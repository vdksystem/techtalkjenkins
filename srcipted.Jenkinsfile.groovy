node(POD_LABEL) {
    def GO = tool name: 'go', type: 'com.cloudbees.jenkins.plugins.customtools.CustomTool'
    try {
        def goHome
        stage('Checkout') {
            cleanWs()
            checkout scm
        }
        stage('Test') {
            withEnv(["PATH=${GO}:${PATH}"]) {
                sh 'go get -u github.com/jstemmer/go-junit-report'
                sh 'go test -v 2>&1 | go-junit-report > report.xml'
            }
        }
        stage("Package") {
            sh 'go build'
        }
        stage("Publish") {
            if (BRANCH_NAME == 'master') {
                sh "docker pull ubuntu:16.04"
                sh "docker push dcr-docker-registry:5000/ubuntu"
            }
        }
        stage("Deploy") {
            if (BRANCH_NAME == 'master') {
                echo "deploy to kubernetes"
            }
        }
    } catch (err) {
        throw err
    } finally {
        switch (currentBuild.result) {
            case 'SUCCESS':
                echo "SUCCESS actions"
                junit '*.xml'
                archiveArtifacts 'app'
                break
        }
    }
}
