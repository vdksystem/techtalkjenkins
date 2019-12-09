node {
    try {
        stage('Checkout') {
//            cleanWs()
            // TODO: change to scm, git doesn't inject env variables
            git 'https://github.com/jglick/simple-maven-project-with-tests.git'
        }
        stage('Test') {
            sh 'go test -v 2>&1 | go-junit-report > report.xml'
        }
        stage("Package") {
            try {
                sh 'go build'
            } catch (e) {
                echo e.message
            } finally {
                junit '*.xml'
                archiveArtifacts 'app'
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
                junit ''
                break
        }
    }
}