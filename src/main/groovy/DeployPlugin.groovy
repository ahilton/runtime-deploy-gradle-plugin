import org.gradle.api.Plugin
import org.gradle.api.Project

class DeployPlugin implements Plugin<Project> {
    void apply(Project project) {

        // Add deploy extension
        def extension = project.extensions.create('deploy', DeployPluginExtension)

        // Add the components container as an extension object
        def components = project.container(DeployableComponent)
        project.extensions.components = components

        // Create deployment task for each component
        components.withType(DeployableComponent.class, { dc ->
            project.task("deploy"+dc.name.capitalize(), type: DeployTask) {
                component = dc
            }
        })

        // Configure defaults
        components.all {
            group = extension.defaultGroup
        }

        // Add a deployAll task which depends on all individual component deploy tasks
        project.task('deployAll').dependsOn(project.tasks.withType(DeployTask))
    }
}