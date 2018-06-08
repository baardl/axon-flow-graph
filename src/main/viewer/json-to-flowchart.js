function fetchJson() {
  return fetch("axon-flow-graph-template.json")
    .then(data=>data.json())
}

async function findStart() {
  var mydata = await fetchJson();
  console.log("mydata: ", mydata);
  /*
  {
      "name": "io.baardl.axon.action.ActionService",
      "type": "CommandGateway",
      "method_name": "create",
      "next": "io.baardl.axon.action.CreateActionCommand"
    }
   */

  var name = mydata.start[0].name;
  return 'st=>start: ' + name + ':>http://www.google.com[blank]\n';
}

function findEnd() {

}

function findNodes() {

}

function findFlow() {

}
