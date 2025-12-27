pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    options {
        timestamps()
    }

    parameters {
        // Add whatever test classes you want as options
        choice(
            name: 'TEST_CLASS',
            choices: [
                'All Tests',
                'CartTest',
                'CheckoutCompleteTest',
                'CheckoutStepOneTest',
                'CheckoutStepTwoTest',
                'FooterComponentTest',
                'HeaderComponentTest',
                'InventoryTest',
                'ItemDetailTest',
                'LoginTest'
            ],
            description: 'Select which test class to execute'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Install Dependencies / Build') {
            steps {
                bat "mvn clean install"
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    if (params.TEST_CLASS == 'All Tests') {
                        bat "mvn clean test"
                    } else {
                        bat "mvn clean test -Dtest=${params.TEST_CLASS}"
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Tests executed successfully'
        }
        failure {
            echo 'Build failed. Please review test reports.'
        }
    }
}
