package models

interface IInitsFromDynamic : IDataModel {
    fun fromDynamic(inD :dynamic) : IDataModel
}

interface IInitsListFromDynamic : IInitsFromDynamic {
    fun listFromDynamic(inD :dynamic) : List<IDataModel>
}
