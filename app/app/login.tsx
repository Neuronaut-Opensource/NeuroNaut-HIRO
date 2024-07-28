import { Button, StyleSheet, TextInput } from 'react-native';
import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';
import { SetStateAction, useState } from 'react';
import { router } from 'expo-router';

export default function LoginScreen() {
    const [text, setText] = useState('');

    const handleInputChange = (input: SetStateAction<string>) => {
        setText(input);
    }

    const handleSubmit = () => {
        alert(`You entered: ${text}`);
        router.replace("/(tabs)");
    }

    return (
        <ThemedView style={styles.container}>
            <ThemedText style={styles.title}>Mover Hiro</ThemedText>
            <ThemedText style={styles.subtitle}>Login</ThemedText>
            <TextInput
                style={{ height: 40, borderColor: 'gray', borderWidth: 1 }}
                onChangeText={handleInputChange}
                value={text}
            />
            <Button
                title="Submit"
                onPress={handleSubmit}
            />
        </ThemedView>
    );
}

const styles = StyleSheet.create({
    container: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 8,
    },
    title: {

    },
    subtitle: {

    }
});
