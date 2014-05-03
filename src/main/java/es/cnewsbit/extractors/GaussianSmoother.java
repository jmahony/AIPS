package es.cnewsbit.extractors;

import es.cnewsbit.C;
import es.cnewsbit.exceptions.InvalidKernelException;

/**
 * Applies a Gaussian smooth to a 1d array
 */
public class GaussianSmoother implements Smoother {

    /**
     *
     * Smooths an array of Smoothables objects, needs a kernel for the smoothing process.
     *
     * @param items array of Smoothables
     * @param kernel kernel to smooth by
     * @return array of smoothed Smoothables
     * @throws InvalidKernelException if the kernel is not odd
     */
    public Smoothable[] smooth(Smoothable[] items, Double[] kernel) throws InvalidKernelException {

        if (kernel.length % 2 == 0)
            throw new InvalidKernelException("Kernel length must be odd");

        int kernelOverlap = (int) Math.floor(kernel.length / 2);

        for (int i = 0; i < items.length; i++) {

            double newRatio = 0;

            for (int j = 0; j < kernel.length; j++) {

                int lineIndex = i - kernelOverlap + j;

                if (lineIndex >= 0 && lineIndex < items.length) {

                    Double valueToSmooth = items[lineIndex].getValueToSmooth();

                    newRatio += valueToSmooth * kernel[j];

                }

            }

            items[i].setSmoothedValue(newRatio);

        }

        return items;

    }

    /**
     *
     * Uses the default kernel
     * TODO: Move the default kernel to this class
     *
     * @param items array of Smoothables
     * @return array of smoothed Smoothables
     * @throws InvalidKernelException if the kernel is not odd
     */
    public Smoothable[] smooth(Smoothable[] items) throws InvalidKernelException {

        return smooth(items, C.SMOOTHING_KERNEL);

    }

}
