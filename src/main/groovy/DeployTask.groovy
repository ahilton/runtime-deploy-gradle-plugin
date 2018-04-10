import dsl.component.ArtifactDescriptor
import dsl.component.DeployableComponent
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class DeployTask extends DefaultTask {

    // https://docs.gradle.org/4.6/userguide/lazy_configuration.html
    @Input
    final Property<DeployableComponent> component = project.objects.property(DeployableComponent)

    @TaskAction
    void performComponentDeploy() {
        def taskName = getName()
        println " >>> Task $taskName"

        def c = component.get()

        if (!c.isDeployable){
            println ">>> Task $taskName [$c.name]. Component is not deployable. Skipping."
            return
        }

        // deploy dependent components before deploying main component
        c.dependencies.each {
            doDeploy(it)
        }

        doDeploy(c)
    }

    boolean doDeploy(ArtifactDescriptor descriptor){
        // [$c.name]. Resolving dependency: $dependency"
        println " >>> Resolving "+descriptor.buildDependencyNotation()

        def dependencyNotation = descriptor.buildDependencyNotation()

        // resolve dependency
        def id = descriptor.getNameAsGradleCompatibleIdentifier()
        def configName = "configuration$id"
        def config = project.configurations.create(configName)
        project.dependencies.add(configName, dependencyNotation)
        def fs = config.resolve()
        println fs
    }
}