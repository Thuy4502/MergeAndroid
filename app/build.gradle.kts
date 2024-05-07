import org.gradle.internal.impldep.bsh.commands.dir

plugins {
    id("com.android.application")
    id ("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.testapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.testapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}
buildscript {
    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}
secrets {
    // Optionally specify a different file name containing your secrets.
    // The plugin defaults to "local.properties"
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"

    // Configure which keys should be ignored by the plugin by providing regular expressions.
    // "sdk.dir" is ignored by default.
    ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
    ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
}

dependencies {
    //map
    implementation ("com.github.jd-alexander:library:1.1.0")

    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.maps:google-maps-services:2.2.0")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")



    implementation("com.google.android.gms:play-services-location:17.0.0")
    implementation("com.google.android.gms:play-services-analytics:17.0.0")

    implementation ("com.google.gms:google-services:4.3.15")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")


    implementation ("io.reactivex.rxjava2:rxjava:2.2.2")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.0.0")
    implementation ("io.reactivex.rxjava2:rxandroid:2.0.2")

    // Chart and graph library
    implementation ("com.github.blackfizz:eazegraph:1.2.5l@aar")
    implementation ("com.nineoldandroids:library:2.4.0")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-auth")

    //call API
    implementation ("com.squareup.retrofit2:retrofit:2.4.0")
    implementation("com.squareup.retrofit2:converter-gson:2.3.0") //convert JSON => Oject
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.4.0")

    //Oject => JSON
    implementation ("com.google.code.gson:gson:2.10.1")

    //show more
//    implementation("com.borjabravo:readmoretextview:2.1.0")

    //load img
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.work:work-runtime:2.9.0")
    implementation("androidx.leanback:leanback:1.0.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")


    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    implementation("com.google.android.material:material:1.11.0")

    implementation("androidx.cardview:cardview:1.0.0")
    implementation("me.relex:circleindicator:2.1.6")
    implementation ("com.github.colourmoon:readmore-textview:v1.0.2")
}