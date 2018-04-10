import dsl.DeployableComponent
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
        def c = component.get()

        if (!c.isDeployable){
            println ">>> Task $taskName [$c.name]. Component is not deployable. Skipping."
            return
        }

        // attempt to deploy dependent components
        c.dependencies.each {
            println it.buildDependencyNotation()
        }

        def dependency = c.buildDependencyNotation()

        println " >>> Task $taskName [$c.name]. Resolving dependency: $dependency"

        // resolve dependency
        def id = c.getNameAsGradleCompatibleIdentifier()
        def configName = "configuration$id"
        def config = project.configurations.create(configName)
        project.dependencies.add(configName, dependency)
        def fs = config.resolve()
        println fs

        //TODO:: pass file and artifact id
    }
}