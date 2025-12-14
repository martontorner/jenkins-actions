def call(Map spec) {
  if (!spec?.name) error "action: 'name' is required"
  if (!spec?.uses) error "action: 'uses' is required"

  def whenBlock = spec.when
  def postBlock = spec.post

  def actionSpec = spec.findAll { k, _ ->
    !['name', 'when', 'post'].contains(k)
  }

  stage(spec.name) {
    if (whenBlock) {
      when(whenBlock)
    }
    steps {
      executeAction(actionSpec)
    }
    if (postBlock) {
      post(postBlock)
    }
  }
}

def executeAction(Map spec) {
  def parsed = actions.Resolver.parse(spec.uses)
  def inputs = spec.with ?: [:]

  library(
    identifier: "${parsed.owner}-${parsed.repo}@${parsed.ref}",
    retriever: modernSCM([
      $class: 'GitSCMSource',
      remote: "https://github.com/${parsed.owner}/${parsed.repo}.git",
      credentialsId: 'github'
    ])
  )

  def stepName = parsed.path ?: 'action'
  this."${stepName}"(inputs)
}
