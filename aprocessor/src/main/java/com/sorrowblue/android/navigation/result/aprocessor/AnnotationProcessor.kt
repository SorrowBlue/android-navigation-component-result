package com.sorrowblue.android.navigation.result.aprocessor

import com.google.auto.service.AutoService
import com.sorrowblue.androidx.navigation.result.NavigationResult
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypesException
import javax.tools.Diagnostic.Kind.ERROR
import kotlin.reflect.KClass

const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
const val FRAGMENT = "androidx.fragment.app.Fragment"

@AutoService(Processor::class)
class AnnotationProcessor : AbstractProcessor() {
    override fun getSupportedAnnotationTypes() = mutableSetOf(NavigationResult::class.java.name)
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()
    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        roundEnv.getElementsAnnotatedWith(NavigationResult::class.java)
            .forEach {
                when {
                    it.kind != ElementKind.CLASS -> {
                        processingEnv.messager.printMessage(ERROR, "Only classes can be annotated")
                        return true
                    }
                    !processingEnv.typeUtils.isAssignable(
                        it.asType(),
                        processingEnv.elementUtils.getTypeElement(FRAGMENT).asType()
                    )
                    -> {
                        processingEnv.messager.printMessage(
                            ERROR,
                            "Only classes that extend \"$FRAGMENT\" class can be annotated.(${it.asType().asTypeName()})"
                        )
                        return true
                    }
                    else -> processAnnotation(it)
                }
            }
        return false
    }

    private fun KClass<*>.asTypeNameJavaKotlin() = kotlin.runCatching { asTypeName() }.fold({it}) {
        (it as MirroredTypesException).typeMirrors.first().asTypeName()
    }

    private fun processAnnotation(element: Element) {
        val packageName = processingEnv.elementUtils.getPackageOf(element).toString()
        val fileName = "${element.simpleName}NavigationResult"
        val resultType =
            kotlin.runCatching { element.getAnnotation(NavigationResult::class.java).type.asTypeName() }.fold({it}) {
                (it as MirroredTypesException).typeMirrors.first().asTypeName()
            }
        val key = element.getAnnotation(NavigationResult::class.java).key
        val fileBuilder = FileSpec.builder(packageName, fileName)
        val fragment =
            processingEnv.typeUtils.directSupertypes(element.asType()).first().asTypeName()
        fileBuilder.addImport("androidx.navigation.fragment.findNavController", "")
        fileBuilder.addImport("androidx.lifecycle.observe", "")
        TypeSpec.objectBuilder(fileName).addFunction(createObserver(fragment, key, resultType)).build().let(fileBuilder::addType)

        FunSpec.builder("setNavigationResult")
            .addParameter(ParameterSpec.builder("value", resultType).build())
            .receiver(element.asType().asTypeName())
            .addStatement(
                """findNavController().previousBackStackEntry?.savedStateHandle?.set("$key", value)"""
            ).build().let(fileBuilder::addFunction)

        val file = fileBuilder.build()
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        file.writeTo(File(kaptKotlinGeneratedDir))
    }

    private fun createObserver(superType: TypeName, key: String, resultType: TypeName): FunSpec {
        val lambda = LambdaTypeName.get(
            null,
            listOf(ParameterSpec.unnamed(resultType)),
            Unit::class.asTypeName()
        )
        return FunSpec.builder("observe")
            .addParameter("owner", superType)
            .addParameter("callback", lambda)
            .addStatement(
                """
				owner.findNavController().currentBackStackEntry?.savedStateHandle?.let { handle ->
					handle.getLiveData<${resultType}>("$key").observe(owner.viewLifecycleOwner) {
						${if (!resultType.isNullable) "\t\t\t\t\t\tif (it != null) callback.invoke(it)" else "\t\t\t\t\t\tcallback.invoke(it)"}
					}
				}
				""".trimIndent()
            ).build()
    }
}
