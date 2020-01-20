def repoUpdate() {
    sh 'helm repo update'
}

def deploy(String component, cfg, String args = '', Boolean local = true) {
    // Chart name  = component name
    def repo = "falcon-${cfg.ENV.toLowerCase()}"
    fetch(repo, component)
    def chart = "${repo}/${component}"
    def allArgs = args.tokenize(' ')
    allArgs.add("--timeout 600")
    allArgs.add("--namespace ${cfg.namespace}")
    if (cfg.dry) {
        allArgs.add("--dry-run --debug")
    }
    if (local) {
        chart = "${component}/"
        allArgs.add("--reuse-values")
        allArgs.add("-f ${component}/values-${cfg.ENV.toLowerCase()}.yaml")
    }
    upgrade(chart, component, cfg.ENV, allArgs.join(' '))
}

def upgrade(String chart, String name, String env, String args = '') {
    sh 'echo $PATH'
    // chart is either remote chart name or local directory path
    //TODO: I don't like it, should be something global
    switch (env) {
        case 'STG':
            withKubeConfig([credentialsId: 'stg-tiller-token', serverUrl: 'https://7238DFFBE05698F7140599FA7502472F.sk1.eu-west-1.eks.amazonaws.com']) {
                sh "helm upgrade -i ${name} ${chart} ${args}"
            }
            break
        case 'PRD':
            withKubeConfig([credentialsId: 'prd-tiller-token', serverUrl: 'https://EA8A79ADC51AA217B45A2D9A3C8FC3EC.sk1.eu-west-1.eks.amazonaws.com']) {
                sh "helm upgrade -i ${name} ${chart} ${args}"
            }
            break
        default:
            sh "helm upgrade -i ${name} ${chart} ${args}"
            break
    }
}