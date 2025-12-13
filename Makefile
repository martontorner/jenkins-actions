.DEFAULT_GOAL := help
.PHONY : prepare

help: ## This help.
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

prepare:  ## Prepare the development environment.
	@git config --local include.path ../.gitconfig
