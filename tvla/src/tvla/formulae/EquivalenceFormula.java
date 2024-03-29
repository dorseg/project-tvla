package tvla.formulae;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;

import tvla.core.TVS;
import tvla.core.assignments.Assign;
import tvla.exceptions.SemanticErrorException;
import tvla.logic.Kleene;
import tvla.predicates.Predicate;
import tvla.util.NoDuplicateLinkedList;

/** A logical equivalence formula 
 * @author Tal Lev-Ami 
 */
public class EquivalenceFormula extends Formula {
	private Formula leftSubFormula;    
	private Formula rightSubFormula;

	/** Create a new logical equivalence formula from the given sub formulae. */
	public EquivalenceFormula(Formula leftSubFormula, Formula rightSubFormula) {
		super();
		this.leftSubFormula = leftSubFormula;
		this.rightSubFormula = rightSubFormula;
	}

	/** Create a copy of the formula */
	public Formula copy() {
		return new EquivalenceFormula(leftSubFormula.copy(), rightSubFormula.copy());
	}

	/** Substitute the given variable name to a new name. */
	public void substituteVar(Var from, Var to) {
		leftSubFormula.substituteVar(from, to);
		rightSubFormula.substituteVar(from, to);
		freeVars = null;
	}

	/** Substitute variables in parallel according to the sub map. */
	public void substituteVars(Map<Var, Var> sub) {
		leftSubFormula.substituteVars(sub);
		rightSubFormula.substituteVars(sub);
		freeVars = null;
	}
	
	/** Return the left sub formula */
	public Formula left() {
		return leftSubFormula;
	}

	/** Return the right sub formula */
	public Formula right() {
		return rightSubFormula;
	}

	/** Evaluate the formula on the given structure and assignment. */
	public Kleene eval(TVS s, Assign assign) {
		Kleene leftResult = leftSubFormula.eval(s, assign);
		Kleene rightResult = rightSubFormula.eval(s, assign);

		return Kleene.and(Kleene.or(Kleene.not(leftResult), rightResult),
						  Kleene.or(Kleene.not(rightResult), leftResult));
	}

	/** Prepare this formula for the new structure */
	public boolean askPrepare(TVS s) {
        boolean leftPrepare = leftSubFormula.askPrepare(s);
        boolean rightPrepare = rightSubFormula.askPrepare(s);
        return leftPrepare || rightPrepare;
	}

	/** Calculate and return the free variables for this formula. */
	public List<Var> calcFreeVars() {
		List<Var> result = new NoDuplicateLinkedList<Var>();
		result.addAll(leftSubFormula.freeVars());
		result.addAll(rightSubFormula.freeVars());
        for (Var bound : boundVars()) {
            if (result.contains(bound)) {
                throw new SemanticErrorException("Formula " + this + " has " + bound + " as both free & bound variable");
            }
        }
		return result;
	}

	/** Calculate and return variables bound in this formula or subformulae. */
	public List<Var> calcBoundVars() {
		List<Var> result = new NoDuplicateLinkedList<Var>();
		result.addAll(leftSubFormula.boundVars());
		result.addAll(rightSubFormula.boundVars());
		return result;
	}

	/** Return a human readable representation of the formula. */
	public String toString() {
		return "(" + leftSubFormula.toString() + " <-> " + rightSubFormula.toString() + ")";
	}

	/** Equate the this formula with the given fomula by structure. */
	public boolean equals(Object o) {
		if (!(o instanceof EquivalenceFormula))
			return false;
		EquivalenceFormula other = (EquivalenceFormula) o;
		return this.leftSubFormula.equals(other.leftSubFormula) &&
			   this.rightSubFormula.equals(other.rightSubFormula);
	}

	public int hashCode() {
		return leftSubFormula.hashCode() * 31 + rightSubFormula.hashCode();
	}
	
	public int ignoreVarHashCode() {
		return leftSubFormula.ignoreVarHashCode() * 31 + rightSubFormula.ignoreVarHashCode();
	}
	
	public Set<Predicate> getPredicates() {
		if (predicates != null) {
			return predicates;
		}
		predicates = new LinkedHashSet<Predicate>();
		predicates.addAll(leftSubFormula.getPredicates());
		predicates.addAll(rightSubFormula.getPredicates());
		return predicates;
	}


	/** Calls the specific accept method, based on the type of this formula
	 * (Visitor pattern).
	 * @author Roman Manevich.
	 * @since tvla-2-alpha November 18 2002, Initial creation.
	 */
    @Override
    public <T> T visit(FormulaVisitor<T> visitor) {
		return visitor.accept(this);
	}
	
	public void traversePostorder(FormulaTraverser t) {
		leftSubFormula.traverse(t);
		rightSubFormula.traverse(t);
		t.visit(this);
	}

	public void traversePreorder(FormulaTraverser t) {
		t.visit(this);
		leftSubFormula.traverse(t);
		rightSubFormula.traverse(t);
	}

//	public Formula pushBackNegations(boolean negated) {
//        if (!negated) {
//            leftSubFormula = leftSubFormula.pushBackNegations(false);
//            rightSubFormula = rightSubFormula.pushBackNegations(false);
//            return this;
//        } else {
//            leftSubFormula = leftSubFormula.pushBackNegations(true);
//            rightSubFormula = rightSubFormula.pushBackNegations(false);
//            return this;
//        }
//    }

}