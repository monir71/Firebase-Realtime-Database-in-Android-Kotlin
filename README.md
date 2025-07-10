Dependencies:

```
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.footballdilalpur"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.footballdilalpur"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
```

Code:

```
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                val token = task.result
                Log.d("FirebaseToken", "Token: $token")
            }
            else
            {
                Log.w("FirebaseToken", "Fetching FCM registration token failed", task.exception)
            }
        }

        val userRef = FirebaseDatabase.getInstance().getReference("CopyRight")
        userRef.setValue("I am here to help you.")

        val contactRef = FirebaseDatabase.getInstance().getReference("Contact")
        val contactModel = ContactModel("Md Moniruzzaman", "01710389323")
        val contactId : String = contactRef.push().key ?: ""
        contactRef.child(contactId).setValue(contactModel)

        contactRef.child("Contact").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children){
                    val name = child.child("name").getValue(ContactModel::class.java)
                    val phone = child.child("phoneNumber").getValue(ContactModel::class.java)
                    Log.d("MyData", "Name: $name and Phone: $phone")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DB Error : ", error.toString())
            }
        })



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

```
