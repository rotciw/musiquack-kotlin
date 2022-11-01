terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
      version = "4.37.0"
    }
  }

  backend "s3" {
    bucket = "musiquack-terraform-state"
    key    = "musiquack-kotlin/service.terraform.tfstate"
    region = "eu-west-1"
  }

}

locals {
  application_name = "musiquack-kotlin"
  environment = "service"

  tags = {
    managedBy = "terraform"
    application = local.application_name
  }
}

provider "aws" {
  region = "eu-west-1"
}

resource "aws_ecr_repository" "image" {
  name = local.application_name
  image_tag_mutability = "MUTABLE"

  tags = local.tags

  image_scanning_configuration {
    scan_on_push = false
  }
}