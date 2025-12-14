package actions

class Resolver {

  static Map parse(String uses) {
    if (!uses.contains('@')) {
      error "Invalid uses format: ${uses}"
    }

    def (repoPart, ref) = uses.split('@', 2)
    def parts = repoPart.split('/')

    if (parts.size() < 2) {
      error "Invalid uses format: ${uses}"
    }

    return [
      owner: parts[0],
      repo : parts[1],
      path : parts.size() > 2 ? parts[2..-1].join('_') : null,
      ref  : ref
    ]
  }
}
