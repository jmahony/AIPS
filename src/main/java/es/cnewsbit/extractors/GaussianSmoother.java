package es.cnewsbit.extractors;

import es.cnewsbit.C;
import es.cnewsbit.exceptions.InvalidKernelException;

/**
 * Created by joshmahony on 27/04/2014.
 */
public class GaussianSmoother implements Smoother {

    /**
     *
     * Smooths an array of HTMLLine objects, needs a kernel for the smoothing process.
     *
     * @return an array of HTMLLine objects with the smoothed ratio populated
     * @exception es.cnewsbit.exceptions.InvalidKernelException if the kernel has an even number of elements
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
