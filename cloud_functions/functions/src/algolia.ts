import * as admin from 'firebase-admin';
import algoliasearch from "algoliasearch";

// const appId = "86LDTYD1VW";
// const adminKey = "75341d810d7ab253a97cdd8709da562a";
// const indexId = "doctorsNew";


const appId = "RVX0QK5HY4";
const adminKey = "f77556e412b39279b4e81298c847fe0c";
const indexId = "doctorsNew";


let algoliaClient = algoliasearch(appId,adminKey);
let {saveObjects} = algoliaClient.initIndex(indexId);

const indexAllSubCall = function(typeStr:string):Promise<any>{
    let collections = "hospitals";
    if (typeStr === "doctor")
        collections="doctors";

    return admin.firestore().collection(collections).get().then((querySnap)=>{
        let objs:Array<any> = [];
        querySnap.forEach(query=>{
            let q = query.data();
            q["objectID"] = query.id;
            q["hospitalId"] = query.id;
            q["indexType"] = typeStr;
            objs.push(q)
        });
        saveObjects(objs).then(console.log).catch(console.log);
    }).catch(console.log)

};

const indexAll= function () {
    indexAllSubCall("hospital").then(()=>console.log("done hospitals")).catch(console.log);
    indexAllSubCall("doctor").then(()=>console.log("done doctor")).catch(console.log);
};


const doManually=()=>{
    admin.initializeApp({
        credential: admin.credential.cert("./service.json"),
        databaseURL: "https://jeevan-b9882.firebaseio.com"
    });
    indexAll()
};

doManually();
