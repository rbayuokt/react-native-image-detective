import { useCallback, useState } from 'react';
import type { ImageLabelerResult } from '../../../src/types';

export const useForm = <T extends object>(initialState: T) => {
  const [form, setForm] = useState<T>(initialState);

  const inputChangeHandler = useCallback(
    (name: keyof T) =>
      (
        value:
          | string
          | string[]
          | boolean
          | number
          | Date
          | Array<ImageLabelerResult>
      ) =>
        setForm((old) => ({ ...old, [name]: value })),
    []
  );

  const resetForm = useCallback(() => {
    setForm(initialState);
  }, [initialState]);

  return {
    form,
    inputChangeHandler,
    resetForm,
  };
};
