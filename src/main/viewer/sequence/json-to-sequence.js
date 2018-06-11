function fetchJson() {
  return fetch("../axon-flow-graph-template.json")
    .then(data=>data.json())
}

async function buildFlow() {
  var mydata = await fetchJson();
  // var flow_described = findStart(mydata) + findEnd(mydata) + findNodes(mydata) + findFlow(mydata);
  var flow_described = findStart(mydata);
  console.log("Flow: ", flow_described);
  return flow_described;

}
function findStart(mydata) {

  console.log("mydata: ", mydata);

  var nodes = "";
  for(var i in mydata.start) {
    var id = mydata.start[i].name;
    var name = mydata.start[i].name;
    nodes = nodes + 'participant ' +  id + '\n';
  }

  var name = mydata.start[0].name;
  return nodes;
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

function findFlow(mydata) {

  var flow = "";
  for (var startIndex in mydata.start) {
    var start_id = mydata.start[startIndex].name;
    // flow = flow + start_id + '->io.baardl.axon.action.ActionCommandHandler->io.baardl.axon.action.ActionEventObserver->e \n '
    var handler = findCommandHandler(mydata,mydata.start[startIndex].next);
    flow = flow + start_id + '->' +handler + '\n';
  }

  for (const commandHandler of mydata.command_handlers) {
    var commandId = commandHandler.name;
    var handler = findEventHandler(mydata, commandHandler.next);
    flow = flow + commandId + '->' + handler + '\n';
  }
  // return start_id + '->io.baardl.axon.action.ActionCommandHandler->io.baardl.axon.action.ActionEventObserver->e';
  return flow;
}

function findCommandHandler(mydata, next) {
  var handlerName = "";
  for(var i in mydata.command_handlers) {
    var handle = mydata.command_handlers[i].handle;
    if (handle === next) {
      handlerName = mydata.command_handlers[i].name;
      break;
    }
  }
  return handlerName;
}

function findEventHandler(mydata, next) {
  var handlerName = "";
  for(var i in mydata.event_handlers) {
    var handle = mydata.event_handlers[i].handle;
    if (handle === next) {
      handlerName = mydata.event_handlers[i].name;
      break;
    }
  }
  return handlerName;
}
