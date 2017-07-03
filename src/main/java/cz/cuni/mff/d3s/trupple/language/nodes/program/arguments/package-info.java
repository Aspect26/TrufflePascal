/**
 * Package containing node that take care of assigning program arguments to variables. Combination of
 * {@link cz.cuni.mff.d3s.trupple.language.nodes.variables.write.SimpleAssignmentNode} and
 * {@link cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode} cannot be used like it is used for function
 * because some types of arguments are treated a little differently.
 */
package cz.cuni.mff.d3s.trupple.language.nodes.program.arguments;