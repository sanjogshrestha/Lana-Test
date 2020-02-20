# Lana-Test
=============================================

This test app showcases the following Architecture Components:

* [Room](https://developer.android.com/topic/libraries/architecture/room.html)
* [ViewModels](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html)
* [LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData.html)
* [DATA BINDING](https://developer.android.com/topic/libraries/view-binding)
* [Dagger](https://dagger.dev/android)

Introduction
-------------

### Features

This sample contains two screens: a list of products and a checkout view, that shows the selected products, their price, quantity and the offer(if applicable).

The app uses a Model-View-ViewModel (MVVM) architecture . Each of the fragments corresponds to a MVVM View. The ViewModel communicate  using LiveData and the following design principles:

* ViewModel objects don't have references to activities, fragments, or Android views. That would cause leaks on configuration changes, such as a screen rotation, because the system retains a ViewModel across the entire lifecycle of the corresponding view.

* ViewModel objects expose data using `LiveData` objects. `LiveData` allows you to observe changes to data across multiple components of your app without creating explicit and rigid dependency paths between them.

The database is created using Room and it has two entities: a `ProductEntity` and a `ProductCheckoutEntity` that generate corresponding SQLite tables.

To access the data and execute queries, you use a DAO ([Data Access Object])(https://developer.android.com/topic/libraries/architecture/room.html#daos). For example, a product is loaded with the following query:

```java
   @Query("SELECT * FROM product_checkout")
   fun loadAllCheckout(): LiveData<List<ProductCheckoutEntity>>
```