async function fetchJson() {
  var jsonobj = null;
  $.getJSON("axon-flow-graph-template.json", function (data) {
    jsonobj = data;
  });
  return jsonobj;
}

function findStart() {
  var mydata = fetchJson();
console.log("mydata: ", mydata);

  return 'st=>start: sstart:>http://www.google.com[blank]\n';
}

function findEnd() {

}

function findNodes() {

}

function findFlow() {

}
