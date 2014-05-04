package es.cnewsbit.extractors;

import es.cnewsbit.C;
import es.cnewsbit.exceptions.InvalidKernelException;

public class GaussianSmoother implements Smoother {

    /**
     *
     * Applies a gaussian smoothing process a 1 dimensional array. It goes over
     * each array element adjusting the value to smooth according to the kernel.
     *
     * The kernel must have an odd number of elements otherwise we will not know
     * how to apply it.
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

    public Smoothable[] smooth(Smoothable[] items) throws InvalidKernelException {

        return smooth(items, C.SMOOTHING_KERNEL);

    }

}
