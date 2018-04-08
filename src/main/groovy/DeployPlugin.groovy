import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.dsl.DependencyHandler

class DeployPlugin implements Plugin<Project> {
    void apply(Project project) {

        def configurations = project.configurations
        configurations.create("alex")

        def dependencies = project.dependencies


        // Add the 'greeting' extension object
        def extension = project.extensions.create('deploy', DeployPluginExtension)
        // Add a task that uses configuration from the extension object
        project.task('hello', type: Deploy) {
            doLast {
                println extension.message
            }
        }
    }
}