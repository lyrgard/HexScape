package fr.lyrgard.hexScape.gui.desktop.layout;

import java.util.List;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.manager.LayoutManager;
import de.lessvoid.nifty.tools.SizeValue;

public class FlowLayout  implements LayoutManager {

	/**
	 * Layout the elements.
	 *
	 * @param root the root element
	 * @param children the children
	 */
	public final void layoutElements(final LayoutPart root, final List < LayoutPart > children) {
		if (isInvalid(root, children)) {
			return;
		}

		int rootBoxX = getRootBoxX(root);
		int rootBoxY = getRootBoxY(root);
		int rootBoxWidth = getRootBoxWidth(root);
		int rootBoxHeight = getRootBoxHeight(root);

		int x = rootBoxX;
		int maxHeightOfRow = 0;
		int currentRowWidth = 0;
		int currentY = rootBoxY;
		for (int i = 0; i < children.size(); i++) {
			LayoutPart current = children.get(i);
			Box box = current.getBox();
			BoxConstraints boxConstraints = current.getBoxConstraints();

			int elementWidth;

			elementWidth = calcElementWidth(children, rootBoxWidth, boxConstraints, 0);
			box.setWidth(elementWidth);

			int elementHeight = processHeightConstraint(rootBoxHeight, box, boxConstraints, 0);
			box.setHeight(elementHeight);
			
			
			if (currentRowWidth + elementWidth + leftMargin(boxConstraints, rootBoxWidth) + rightMargin(boxConstraints, rootBoxWidth) > rootBoxWidth) {
				// new line
				currentY += maxHeightOfRow + 2;
				currentRowWidth = 0;
				maxHeightOfRow = 0;
				x = rootBoxX;
			} 
			
			// append to the current line
			maxHeightOfRow = Math.max(maxHeightOfRow, box.getHeight());
			if (currentY + maxHeightOfRow - rootBoxY > rootBoxHeight) {
				setNewRootBoxHeight(root, currentY + maxHeightOfRow - rootBoxY);
			}

			x += leftMargin(boxConstraints, rootBoxWidth);
			box.setX(x);

			box.setY(currentY);

			x += elementWidth + rightMargin(boxConstraints, rootBoxWidth);
			currentRowWidth = x - rootBoxX; 
		}
	}

	private int leftMargin(final BoxConstraints boxConstraints, final int rootBoxWidth) {
		return boxConstraints.getMarginLeft().getValueAsInt(rootBoxWidth);
	}

	private int rightMargin(final BoxConstraints boxConstraints, final int rootBoxWidth) {
		return boxConstraints.getMarginRight().getValueAsInt(rootBoxWidth);
	}

	private int processHeightConstraint(final int rootBoxHeight, final Box box, final BoxConstraints constraint, final int elementWidth) {
		if (hasHeightConstraint(constraint)) {
			if (constraint.getHeight().hasWidthSuffix()) {
				return constraint.getHeight().getValueAsInt(elementWidth);
			}
			return constraint.getHeight().getValueAsInt(rootBoxHeight);
		} else {
			return rootBoxHeight;
		}
	}

	private boolean hasHeightConstraint(final BoxConstraints constraint) {
		return constraint != null && constraint.getHeight() != null && !constraint.getHeight().hasWildcard();
	}

	private int calcElementWidth(final List < LayoutPart > children, final int rootBoxWidth, final BoxConstraints boxConstraints, final int elementHeight) {
		if (boxConstraints.getWidth() != null) {
			int h = (int) boxConstraints.getWidth().getValue(rootBoxWidth);
			if (boxConstraints.getWidth().hasHeightSuffix()) {
				h = (int) boxConstraints.getWidth().getValue(elementHeight);
			}
			if (h != -1) {
				return h;
			}
		}
		return getMaxNonFixedWidth(children, rootBoxWidth);
	}

	/**
	 *
	 * @param elements the child elements the max width is going
	 * to be calculated
	 * @param parentWidth the width of the parent element
	 * @return max non fixed width
	 */
	private int getMaxNonFixedWidth(
			final List < LayoutPart > elements,
			final int parentWidth
			) {
		int maxFixedWidth = 0;
		int fixedCount = 0;
		for (int i = 0; i < elements.size(); i++) {
			LayoutPart p = elements.get(i);
			BoxConstraints original = p.getBoxConstraints();

			if (original.getWidth() != null) {
				if (original.getWidth().isPercentOrPixel()) {
					maxFixedWidth += original.getWidth().getValue(parentWidth);
					fixedCount++;
				}
			}
		}

		int notFixedCount = elements.size() - fixedCount;
		if (notFixedCount > 0) {
			return (parentWidth - maxFixedWidth) / notFixedCount;
		} else {
			return (parentWidth - maxFixedWidth);
		}
	}

	/**
	 * @param children children elements of the root element
	 * @return new calculated SizeValue
	 */
	public final SizeValue calculateConstraintWidth(final LayoutPart root, final List < LayoutPart > children) {
		return root.getSumWidth(children);
	}

	/**
	 * @param children children elements of the root element
	 * @return new calculated SizeValue
	 */
	public final SizeValue calculateConstraintHeight(final LayoutPart root, final List < LayoutPart > children) {
		int rootBoxWidth = getRootBoxWidth(root);
		int rootBoxHeight = getRootBoxHeight(root);
		
		int maxHeightOfRow = 0;
		int currentRowWidth = 0;
		int currentHeight = 0;
		for (int i = 0; i < children.size(); i++) {
			LayoutPart current = children.get(i);
			Box box = current.getBox();
			BoxConstraints boxConstraints = current.getBoxConstraints();

			int elementWidth;

			elementWidth = calcElementWidth(children, rootBoxWidth, boxConstraints, 0);

			int elementHeight = processHeightConstraint(rootBoxHeight, box, boxConstraints, 0);
			
			
			if (currentRowWidth + elementWidth + leftMargin(boxConstraints, rootBoxWidth) + rightMargin(boxConstraints, rootBoxWidth) > rootBoxWidth) {
				// new line
				currentHeight += maxHeightOfRow + 2;
				currentRowWidth = 0;
				maxHeightOfRow = 0;
			} 
			
			// append to the current line
			maxHeightOfRow = Math.max(maxHeightOfRow, elementHeight); 
			currentRowWidth += elementWidth + leftMargin(boxConstraints, rootBoxWidth) + rightMargin(boxConstraints, rootBoxWidth);
		}
		
		currentHeight += maxHeightOfRow;
		
		return new SizeValue(currentHeight + "px");
	}

	private boolean isInvalid(final LayoutPart root, final List <LayoutPart> children) {
		return root == null || children == null || children.size() == 0;
	}

	private int getRootBoxX(final LayoutPart root) {
		return root.getBox().getX() + root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getBox().getWidth());
	}

	private int getRootBoxY(final LayoutPart root) {
		return root.getBox().getY() + root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getBox().getHeight());
	}

	private int getRootBoxWidth(final LayoutPart root) {
		return root.getBox().getWidth() - root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getBox().getWidth()) - root.getBoxConstraints().getPaddingRight().getValueAsInt(root.getBox().getWidth());
	}

	private int getRootBoxHeight(final LayoutPart root) {
		return root.getBox().getHeight() - root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getBox().getHeight()) - root.getBoxConstraints().getPaddingBottom().getValueAsInt(root.getBox().getHeight());
	}
	
	private void setNewRootBoxHeight(final LayoutPart root, final int containerSize) {
		root.getBox().setHeight(containerSize + root.getBoxConstraints().getPaddingTop().getValueAsInt(containerSize) + root.getBoxConstraints().getPaddingBottom().getValueAsInt(containerSize));
		root.getBoxConstraints().setHeight(new SizeValue(String.valueOf(root.getBox().getHeight())));
	}
}
