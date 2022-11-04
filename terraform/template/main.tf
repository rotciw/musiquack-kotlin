resource "aws_dynamodb_table" "musiquack_users_table" {
  name = "${var.application_name}-${local.environment}-users"
  billing_mode = "PAY_PER_REQUEST"
  hash_key = "userId"


  attribute {
    name = "userId"
    type = "S"
  }

  tags = {
    Name = "dynamodb-musiquack-users"
    Environment = local.environment
  }
}

locals {
  environment = "dev"
}

resource "aws_iam_role" "app_runner_iam_role" {
  name = "${var.application_name}-${var.environment}-app-runner"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Principal = {
          Service = [
            "build.apprunner.amazonaws.com",
            "tasks.apprunner.amazonaws.com",
          ]
        }
        Effect = "Allow"
        Sid = ""
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "application_image_builder" {
  role = aws_iam_role.app_runner_iam_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSAppRunnerServicePolicyForECRAccess"
}


resource "aws_iam_role_policy" "dynamodb_app_runner_policy" {
  name = "dynamodb_app_runner_policy"
  role   = aws_iam_role.app_runner_iam_role.id
  policy = jsonencode({
    "Version": "2012-10-17"
    "Statement": [
      {
        "Effect": "Allow",
        "Action": ["dynamodb:*"],
        "Resource": aws_dynamodb_table.musiquack_users_table.arn
      }
    ]
  })
}

resource "aws_apprunner_service" "application" {
  service_name = "${var.application_name}-${var.environment}"

  health_check_configuration {
    interval = 20
    path = "/health"
    protocol = "HTTP"
    healthy_threshold = 2
    unhealthy_threshold = 3
    timeout = 2
  }

  instance_configuration {
    instance_role_arn = aws_iam_role.app_runner_iam_role.arn
  }

  source_configuration {
    auto_deployments_enabled = false

    image_repository {
      image_configuration {
        port = "8080"
        runtime_environment_variables = {
          CORS_ORIGIN = var.cors_origin
          DYNAMODB_TABLE_NAME = aws_dynamodb_table.musiquack_users_table.name
          SPOTIFY_CLIENTSECRET = var.spotify_clientsecret
          SPOTIFY_CALLBACK_URI = var.spotify_redirect_uri
          SPOTIFY_CLIENTID = var.spotify_clientid
        }
      }
      image_identifier      = "${var.ecr_image_url}:${var.image_tag}"
      image_repository_type = "ECR"
    }

    authentication_configuration {
      access_role_arn = aws_iam_role.app_runner_iam_role.arn
    }
  }

  tags = var.tags
}

resource "aws_secretsmanager_secret" "spotify_client_secret" {
  name = "${var.application_name}-${var.environment}/spotify_client_secret"
  recovery_window_in_days = 0
  force_overwrite_replica_secret = true
  tags = var.tags
}
