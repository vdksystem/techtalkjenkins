import static com.lohika.GlobalConfig.defaults

def call(config = [:]) {
    def cfg = defaults << config
    return cfg
}