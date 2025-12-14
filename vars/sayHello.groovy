def call(Map args = [:]) {
  def spec = cli.Args.create(this, [
    name: cli.Arg.optional(String, "World"),
  ])

  def data = spec.parse(args)

  echo "Hello, ${data.name}."
}
