podTemplate(yaml: """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: golang
    image: golang:1.8.0
    command: ['cat']
    tty: true
""") {
    node(POD_LABEL) {
        try {
            def goHome
            stage('Checkout') {
                cleanWs()
                checkout scm
            }
            stage('Test') {
                container('golang') {
                    sh 'go get -u github.com/jstemmer/go-junit-report'
                    sh 'go test -v 2>&1 | go-junit-report > report.xml'
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