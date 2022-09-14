package by.lebedev.nanopoolmonitoring.utils

import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.annotation.Px

/**
 * Used to specify individual padding values programmatically.
 *
 * @param left Left padding.
 * @param top Top padding.
 * @param right Right padding.
 * @param bottom Bottom padding.
 * decoration.
 * @param paddingType Unit / Type of the given paddings.
 */
data class PaddingView(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val paddingType: PaddingType
) {


    enum class PaddingType {
        PX,
        DP,
        RESOURCE
    }


    companion object {


        /**
         * Constant to unidentified padding. If during set up padding to view padding value
         * is [NO_VALUE_SET], then view padding will be assigned to zero
         */
        const val NO_VALUE_SET = 0

        /**
         * @param paddingRes Padding as dimension resource.
         */
        fun resource(@DimenRes paddingRes: Int): PaddingView {
            return PaddingView(
                paddingRes, paddingRes, paddingRes, paddingRes, PaddingType.RESOURCE
            )
        }

        /**
         * @param leftRes Left padding as dimension resource.
         * @param topRes Top padding as dimension resource.
         * @param rightRes Right padding as dimension resource.
         * @param bottomRes Bottom padding as dimension resource.
         */
        fun resource(
            @DimenRes leftRes: Int = NO_VALUE_SET,
            @DimenRes topRes: Int = NO_VALUE_SET,
            @DimenRes rightRes: Int = NO_VALUE_SET,
            @DimenRes bottomRes: Int = NO_VALUE_SET
        ): PaddingView {
            return PaddingView(
                leftRes, topRes, rightRes, bottomRes, PaddingType.RESOURCE
            )
        }


        /**
         * @param paddingDp Padding in dp.
         */
        fun dp(
            @Dimension(unit = Dimension.DP) paddingDp: Int
        ): PaddingView {
            return PaddingView(
                paddingDp,
                paddingDp,
                paddingDp,
                paddingDp,
                PaddingType.DP
            )
        }

        /**
         * @param leftDp Left padding in dp.
         * @param topDp Top padding in dp.
         * @param rightDp Right padding in dp.
         * @param bottomDp Bottom padding in dp.
         */
        fun dp(
            @Dimension(unit = Dimension.DP) leftDp: Int = NO_VALUE_SET,
            @Dimension(unit = Dimension.DP) topDp: Int = NO_VALUE_SET,
            @Dimension(unit = Dimension.DP) rightDp: Int = NO_VALUE_SET,
            @Dimension(unit = Dimension.DP) bottomDp: Int = NO_VALUE_SET
        ): PaddingView {
            return PaddingView(leftDp, topDp, rightDp, bottomDp, PaddingType.DP)
        }

        /**
         * @param paddingPx Padding in px.
         */
        fun px(
            @Dimension(unit = Dimension.DP) paddingPx: Int
        ): PaddingView {
            return PaddingView(
                paddingPx,
                paddingPx,
                paddingPx,
                paddingPx,
                PaddingType.PX
            )
        }

        /**
         * @param leftPx Left padding in pixels.
         * @param topPx Top padding in pixels.
         * @param rightPx Right padding in pixels.
         * @param bottomPx Bottom padding in pixels.
         */
        fun px(
            @Px leftPx: Int = NO_VALUE_SET,
            @Px topPx: Int = NO_VALUE_SET,
            @Px rightPx: Int = NO_VALUE_SET,
            @Px bottomPx: Int = NO_VALUE_SET
        ): PaddingView {
            return PaddingView(leftPx, topPx, rightPx, bottomPx, PaddingType.PX)
        }
    }

}