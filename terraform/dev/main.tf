terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
      version = "4.37.0"
    }
  }

  backend "s3" {
    bucket = "musiquack-terraform-state"
    key    = "musiquack-kotlin/dev.terraform.tfstate"
    region = "eu-west-1"
  }
}

locals {
  application_name = "musiquack-kotlin"

  environment = "dev"
  // Git tag is used as Docker image tag
  last_commit_sha = trimspace(file("../../.git/${trimspace(trimprefix(file("../../.git/HEAD"), "ref:"))}"))

  tags = {
    managedBy = "terraform"
    application = local.application_name
    environment = "dev"
  }
}

provider "aws" {
  region = "eu-west-1"
}

data "aws_ecr_repository" "image" {
  name = local.application_name
}

module "application" {
  source = "../template"

  application_name = local.application_name
  environment = local.environment
  tags = local.tags

  ecr_image_url = data.aws_ecr_repository.image.repository_url
  image_tag = local.last_commit_sha
  cors_origin   = "https://musiquack.com"
  spotify_clientid = var.spotify_clientid
  spotify_clientsecret = var.spotify_clientsecret
  spotify_redirect_uri = "https://musiquack.com/auth/spotify/callback"
}