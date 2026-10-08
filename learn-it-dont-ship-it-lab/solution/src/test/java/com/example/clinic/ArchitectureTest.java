package com.example.clinic;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Reference solution for "Learn it, don't ship it".
 *
 * ArchUnit imports every compiled class under com.example.clinic and checks each
 * rule below. Each static ArchRule field marked @ArchTest runs as its own test.
 */
@AnalyzeClasses(packages = "com.example.clinic")
class ArchitectureTest {

    // Rule 1: controllers never talk to repositories directly.
    // "Depend on" covers fields, constructor parameters and method calls, not just calls.
    @ArchTest
    static final ArchRule controllers_do_not_use_repositories =
            noClasses().that().resideInAPackage("..web..")
                    .should().dependOnClassesThat().resideInAPackage("..repository..");

    // Rule 2: the domain depends on nothing else in the app.
    // "java.." is needed because String, Object, Record and LocalDate count as dependencies too.
    @ArchTest
    static final ArchRule domain_depends_only_on_itself_and_java =
            classes().that().resideInAPackage("..domain..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("..domain..", "java..");

    // Rule 3: every class named *Repository lives in the repository package.
    @ArchTest
    static final ArchRule repositories_live_in_the_repository_package =
            classes().that().haveSimpleNameEndingWith("Repository")
                    .should().resideInAPackage("..repository..");

    // Stretch goal: the same idea as rule 1, for all three layers at once.
    @ArchTest
    static final ArchRule layers_are_respected =
            layeredArchitecture().consideringOnlyDependenciesInLayers()
                    .layer("Web").definedBy("..web..")
                    .layer("Service").definedBy("..service..")
                    .layer("Repository").definedBy("..repository..")
                    .whereLayer("Web").mayNotBeAccessedByAnyLayer()
                    .whereLayer("Service").mayOnlyBeAccessedByLayers("Web")
                    .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service");
}
