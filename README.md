[![](https://jitpack.io/v/kamrul3288/Expandable-Recyclerview-List-Android.svg)](https://jitpack.io/#kamrul3288/Expandable-Recyclerview-List-Android)
# Expandable Recyclerview Android
<img src="https://user-images.githubusercontent.com/27208120/81283145-ff7e4b00-907d-11ea-97ed-50c5d84cb3af.png" width=300 />

How to
======
Step 1. Add the JitPack repository to your build file
----------------------------------------------------
```
allprojects {
  repositories {
    maven {url 'https://jitpack.io'}
  }
}
```
Step 2. Add the dependency
--------------------------
```
dependencies {
  implementation 'com.github.kamrul3288:Expandable-Recyclerview-List-Android:1.0.2'
}
```
Usage
-----
First, define your custom `Model` class:
```kotlin
data class Category(val name:String,val movieList:List<CategoryList>) : ParentListItem {
    override fun getChildItemList(): List<*> = movieList
    override fun isInitiallyExpanded(): Boolean = false
}
```
```kotlin
data class CategoryList(val name:String)
```

Second create your custom `ViewHolder` class:
```kotlin
class CategoryViewHolder(itemView:View) : ParentViewHolder(itemView) {
    private lateinit var animation: RotateAnimation

    fun bind(category: Category){
        itemView.findViewById<TextView>(R.id.tv_category).text = category.name
    }

    override fun onExpansionToggled(expanded: Boolean) {
        super.onExpansionToggled(expanded)
        animation = if (expanded)
            RotateAnimation(180f,0f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)
        else
            RotateAnimation(-1 * 180f,0f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)

        animation.duration = 200
        animation.fillAfter = true
        itemView.findViewById<ImageView>(R.id.iv_arrow_expand).startAnimation(animation)

    }

    override fun setExpanded(expanded: Boolean) {
        super.setExpanded(expanded)
        if (expanded)itemView.findViewById<ImageView>(R.id.iv_arrow_expand).rotation = 180f
        else itemView.findViewById<ImageView>(R.id.iv_arrow_expand).rotation = 0f
    }
}
```
```kotlin
class CategoryListViewHolder(view:View) : ChildViewHolder(view){
    fun bind(categoryList : CategoryList){
        itemView.findViewById<TextView>(R.id.nameTv).text = categoryList.name
    }
}
```
Third create  custom `Adapter` class:
```kotlin
class CategoryAdapter : ExpandableRecyclerAdapter<CategoryViewHolder, CategoryListViewHolder>(){

    override fun onCreateParentViewHolder(parentViewGroup: ViewGroup
    ): CategoryViewHolder {
        val view = LayoutInflater.from(parentViewGroup.context).inflate(R.layout.item_category, parentViewGroup, false)
        return CategoryViewHolder(view)
    }

    override fun onCreateChildViewHolder(parentViewGroup: ViewGroup): CategoryListViewHolder {
        val view = LayoutInflater.from(parentViewGroup.context).inflate(R.layout.item_category_list, parentViewGroup, false)
        return CategoryListViewHolder(view)
    }

    override fun onBindParentViewHolder(parentViewHolder: CategoryViewHolder, position: Int, parentListItem: ParentListItem) {
        val data = parentListItem as Category
        parentViewHolder.bind(data)
    }

    override fun onBindChildViewHolder(childViewHolder: CategoryListViewHolder, position: Int, childListItem: Any) {
        val data = childListItem as CategoryList
        childViewHolder.bind(data)
    }
}
```
Lastly you'll need either an Activity or Fragment to host your adapter
```kotlin
  private val adapter = CategoryAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = listOf(
            Category("Action", listOf(CategoryList("My Spy"),CategoryList("BloodShot"),CategoryList("Midway"))),
            Category("Drama", listOf(CategoryList("The Godfather"),CategoryList("The Dark Knight"))),
            Category("War", listOf(CategoryList("Apocalypse Now"),CategoryList("Saving Private Ryan")))
        )


        categoryListRv.setHasFixedSize(true)
        categoryListRv.layoutManager = LinearLayoutManager(this)
        adapter.setExpandCollapseListener(object : ExpandCollapseListener {
            override fun onListItemExpanded(position: Int) {
            }

            override fun onListItemCollapsed(position: Int) {

            }

        })
        categoryListRv.adapter = adapter
        adapter.setExpandableParentItemList(data)
    }
```


License
-------
Copyright 2020-2020 Kamrul Hasan

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
