/**
 * FUNCIÓN CREADA PARA USAR EL LOCAL STORAGE DEL NAVEGADOR DEL USUARIO.
 * EL USO PRINCIPAL ES MANEJAR EL JWT DEL USUARIO.
 */
import {useState, useEffect} from 'react';

function useLocalState(defaultValue, key){
    const [value, setValue] = useState(() => {
        const localStorageValue = window.localStorage.getItem(key);
        return localStorageValue !== null ? JSON.parse(localStorageValue) : defaultValue; 
    });

    useEffect(() => {
        window.localStorage.setItem(key, JSON.stringify(value));
    }, [key, value]);  

    return [value, setValue];
}

export {useLocalState}