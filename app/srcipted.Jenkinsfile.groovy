node('jenkins-jenkins-slave') {
    try {
        def goHome
        stage('Checkout') {
            cleanWs()
            checkout scm
        }
        stage('Test') {
            sh 'go get -u github.com/jstemmer/go-junit-report'
            sh 'go test -v 2>&1'
        }
        stage("Package") {
            sh 'go build'
        }
        stage("Publish") {
            if (BRANCH_NAME == 'master') {
                sh "docker build -t dcr-docker-registry:5000/app:v1 ."
                sh "docker push dcr-docker-registry:5000/app:v1"
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