variable "application_name" {
  type = string
}

variable "environment" {
  type = string
}

variable "ecr_image_url" {
  type = string
}

variable "image_tag" {
  type = string
  description = "The tag used to "
}

variable "tags" {
  type = map(string)
}

variable "cors_origin" {
  type = string
}

variable "spotify_clientsecret" {
  type = string
  sensitive = true
}

variable "spotify_clientid" {
  type = string
  sensitive = true
}

variable "spotify_redirect_uri" {
  type = string
}
