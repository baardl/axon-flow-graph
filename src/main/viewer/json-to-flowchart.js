function fetchJson() {
  return fetch("axon-flow-graph-template.json")
    .then(data=>data.json())
}

async function buildFlow() {
  var mydata = await fetchJson();
  var flow_described = findStart(mydata) + findEnd(mydata) + findNodes(mydata) + findFlow();
  /*
    '\n' +
    'st->op1(right)->cond\n' +
    'cond(yes, right)->c2\n' + // conditions can also be redirected like cond(yes, bottom) or cond(yes, right)
    'cond(no)->sub1(left)->op1\n' + // the other symbols too...
    'c2(true)->io->e\n' +
    'c2(false)->op2->e' ; //allow for true and false in conditionals
    */
  debugger;
  return flow_described;

}
function findStart(mydata) {

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
  return 'e=>end:>http://www.google.com\n';

}

function findNodes(mydata) {
  // id + '=>operation: ' + name +'\n' +
  var nodes = "";

  for(var i in mydata.command_handlers) {
    var id = mydata.command_handlers[i].name;
    var name = mydata.command_handlers[i].name;
    nodes = nodes + id + '=>operation: ' + name + '\n';
  }

  for(var i in mydata.event_handlers) {
    var id = mydata.event_handlers[i].name;
    var name = mydata.event_handlers[i].name;
    nodes = nodes + id + '=>subroutine: ' + name + '\n';
  }

   /*
  var nodes = 'op1=>operation: My Operation\n' +
  'op2=>operation: Stuff|current\n' +
  'sub1=>subroutine: My Subroutine\n' +
  'cond=>condition: Yes \n' + // use cond(align-next=no) to disable vertical align of symbols below
  'or No?\n:>http://www.google.com\n' +
  'c2=>condition: Good idea|rejected\n' +
  'io=>inputoutput: catch something...|request\n';
  */
  return nodes;

}

function findFlow() {
  return 'st->io.baardl.axon.action.ActionCommandHandler->io.baardl.axon.action.ActionEventObserver->e';

  /*
   for(var i in mydata.command_handlers) {
    var id = mydata.command_handlers[i].name;
    var name = mydata.command_handlers[i].name;
    nodes = nodes + id + '=>operation: ' + name + '\n';
  }

  for(var i in mydata.event_handlers) {
    var id = mydata.event_handlers[i].name;
    var name = mydata.event_handlers[i].name;
    nodes = nodes + id + '=>subroutine: ' + name + '\n';
  }
   */

}
