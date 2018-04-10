import dsl.ArtifactDescriptor
import dsl.DeployableComponent
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
            project.task("deploy"+dc.getNameAsGradleCompatibleIdentifier(), type: DeployTask) {
                component = dc
            }
        })

        // Configure defaults
        components.all {
            group = extension.defaultGroup
            isDeployable = extension.isDeployable
            // Support dsl nesting. http://mrhaki.blogspot.com.au/2016/02/gradle-goodness-using-nested-domain.html?m=1
            dependencies = project.container(ArtifactDescriptor)
        }

        // Add a deployAll task which depends on all individual component deploy tasks
        project.task('deployAll').dependsOn(project.tasks.withType(DeployTask))
    }
}