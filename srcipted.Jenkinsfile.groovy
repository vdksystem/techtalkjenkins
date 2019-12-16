podTemplate(containers: [
        containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'golang', image: 'golang:1.8.0', ttyEnabled: true, command: 'cat')
]) {
    node('jenkins-jenkins-slave') {
        try {
            def goHome
            stage('Checkout') {
                cleanWs()
                checkout scm
            }
            stage('Test') {
                container('golang') {
                    sh '"$GO_HOME/go" get -u github.com/jstemmer/go-junit-report'
                    sh '"$GO_HOME/go" test -v 2>&1 | go-junit-report > report.xml'
                }
            }
            stage("Package") {
                container('golang') {
                    sh '"$GO_HOME/go" build'
                }
            }
            stage("Publish") {
                if (BRANCH_NAME == 'master') {
                    sh "aws ecr get-login --no-include-email"
                    sh "docker push"
                }
            }
            stage("Deploy") {
                if (BRANCH_NAME == 'master') {
                    sh "deploy to kubernetes"
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
}