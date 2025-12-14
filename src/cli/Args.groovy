package cli

class Arg implements Serializable {
  final Class type
  final Object defaultValue
  final boolean required

  private Arg(Class type, Object defaultValue, boolean required) {
    this.type = type
    this.defaultValue = defaultValue
    this.required = required
  }

  static Arg required(Class type) {
    new Arg(type, null, true)
  }

  static Arg optional(Class type, Object defaultValue) {
    new Arg(type, defaultValue, false)
  }
}

class Args implements Serializable {

  static Args create(def script, Map<String, Arg> spec) {
    new Args(script, spec)
  }

  private final def script
  private final Map<String, Arg> spec

  Args(def script, Map<String, Arg> spec) {
    this.script = script
    this.spec = spec
  }

  Map parse(Map args = [:]) {
    Set unknown = args.keySet() - this.spec.keySet()
    if (!unknown.isEmpty()) {
      script.error("Invalid arguments: ${unknown}")
    }

    Map data = [:]

    this.spec.each { String key, Arg arg ->
      if (args.containsKey(key)) {
        def value = args[key]
        if (!arg.type.isInstance(value)) {
          script.error("Argument '${key}' must be ${arg.type.simpleName}")
        }
        data[key] = value
      } else {
        if (arg.required) {
          script.error("Missing required argument: ${key}")
        }
        data[key] = arg.defaultValue
      }
    }

    return data
  }
}
