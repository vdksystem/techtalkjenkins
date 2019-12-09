node {
    try {
        def goHome
        stage('Checkout') {
            cleanWs()
            checkout scm
        }
        stage('Test') {
            goHome = tool name: 'go', type: 'com.cloudbees.jenkins.plugins.customtools.CustomTool'

            withEnv(["GO_HOME=$goHome"]) {
                sh '"$GO_HOME/go" test'
            }
//            sh 'go test -v 2>&1 | go-junit-report > report.xml'
        }
        stage("Package") {
            try {
                withEnv(["GO_HOME=$goHome"]) {
                    sh '"$GO_HOME/go" build'
                }
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