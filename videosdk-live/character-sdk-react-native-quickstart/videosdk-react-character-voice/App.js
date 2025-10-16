import React, { useEffect, useState } from 'react';
import {
  SafeAreaView,
  TouchableOpacity,
  Text,
  TextInput,
  View,
} from 'react-native';
import {
  MeetingProvider,
  useMeeting,
  useParticipant,
  MediaStream,
  RTCView,
  useCharacter,
} from '@videosdk.live/react-native-sdk';
import { createMeeting, token } from './api';

function JoinScreen(props) {
  return (
    <SafeAreaView
      style={{
        flex: 1,
        backgroundColor: '#F6F6FF',
        justifyContent: 'center',
        paddingHorizontal: 6 * 10,
      }}>
      <Text style={{ color: 'black', alignSelf: 'center', fontSize: 25 }}>
        CharacterSDK
      </Text>

      <Text
        style={{
          alignSelf: 'center',
          fontSize: 22,
          fontStyle: 'italic',
          color: 'grey',
        }}>
      </Text>
      <TextInput
        value={props.name}
        onChangeText={props.setName}
        placeholder={'Enter your name'}
        style={{
          padding: 12,
          borderWidth: 1,
          borderRadius: 6,
        }}
      />
      <TouchableOpacity
        onPress={() => {
          props.getMeetingId();
        }}
        style={{ backgroundColor: '#1178F8', padding: 12, borderRadius: 6, marginTop: 10 }}>
        <Text style={{ color: 'white', alignSelf: 'center', fontSize: 18 }}>
          Start an interaction
        </Text>
      </TouchableOpacity>
    </SafeAreaView>
  );
}

const Button = ({ onPress, buttonText, backgroundColor }) => {
  return (
    <TouchableOpacity
      onPress={onPress}
      style={{
        backgroundColor: backgroundColor,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 12,
        borderRadius: 4,
      }}>
      <Text style={{ color: 'white', fontSize: 12 }}>{buttonText}</Text>
    </TouchableOpacity>
  );
};

function ControlsContainer({ leave, toggleMic }) {
  return (
    <View
      style={{
        padding: 24,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        width: "100%",
        gap: 20
      }}>
      <Button
        onPress={() => {
          toggleMic();
        }}
        buttonText={'Toggle Mic'}
        backgroundColor={'#1178F8'}
      />
      <Button
        onPress={() => {
          leave();
        }}
        buttonText={'Leave'}
        backgroundColor={'#FF0000'}
      />
    </View>
  );
}


function CharacterView() {
  const [characterJoined, setCharacterJoined] = useState(false);
  const {
    join,
    webcamStream,
    webcamOn,
    displayName,
    characterState,
    interrupt
  } = useCharacter(
    {
      id: "quickstart-general-v1",
      characterMode: "auto_pilot",
    },
    {
      onCharacterJoined: () => {
        setCharacterJoined(true);
        console.log("character => character joined");
      },
    }
  );

  useEffect(() => {
    join();
  }, []);

  return (
    <View
      style={{
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
      }}
    >
      {characterJoined ? (
        <>
          <Text style={{ fontSize: 16, textAlign: "center" }}>
            Character: {displayName} | State: {characterState}
          </Text>

          {webcamOn && webcamStream ? (
            <>
              <RTCView
                streamURL={new MediaStream([webcamStream.track]).toURL()}
                objectFit="cover"
                style={{
                  height: 400,
                  width: 350,
                  marginVertical: 8,
                }}
              />
              <Button
                onPress={() => {
                  interrupt();
                }}
                buttonText={'Interrupt Character'}
                backgroundColor={'#1178F8'}
              /></>

          ) : (
            <View
              style={{
                backgroundColor: "grey",
                height: 400,
                width: 350,
                justifyContent: "center",
                alignItems: "center",
                marginVertical: 8,
              }}
            >
              <Text style={{ fontSize: 16, textAlign: "center" }}>NO MEDIA</Text>
            </View>
          )}
        </>
      ) : (
        <View
          style={{
            backgroundColor: "grey",
            height: 300,
            width: 300, // Ensures alignment
            justifyContent: "center",
            alignItems: "center",
            marginVertical: 8,
          }}
        >
          <Text style={{ fontSize: 16, textAlign: "center" }}>
            Please wait while the character joins the interaction
          </Text>
        </View>
      )}
    </View>
  );
}

function MeetingView({ setMeetingId }) {
  const [meetingJoined, setMeetingJoined] = useState(false)
  const { leave, toggleMic, localMicOn } = useMeeting({
    onMeetingJoined: () => {
      console.log("Meeting joined");
      setMeetingJoined(true);
    },
    onMeetingLeft: () => {
      setMeetingId(null)
    }
  });

  return (
    <View style={{
      flex: 1, justifyContent: "center",
      alignItems: "center",
      width: "100%",
    }}>
      <Text style={{ color: 'black', alignSelf: 'center', fontSize: 25, marginTop: 15 }}>
        CharacterSDK
      </Text>

      {!meetingJoined ? (
        <Text style={{ fontSize: 18, padding: 12 }}>Joining the environment...</Text>
      ) :
        <>
          <CharacterView />
          <Text style={{ fontSize: 18, padding: 12 }}>Your mic is {localMicOn ? "ON" : "OFF"}</Text>
          <ControlsContainer
            leave={leave}
            toggleMic={toggleMic}
          />
        </>
      }
    </View>
  );
}

export default function App() {
  const [meetingId, setMeetingId] = useState(null);
  const [name, setName] = useState('');

  const getMeetingId = async id => {
    if (!token) {
      console.log('PLEASE PROVIDE TOKEN IN api.js FROM app.videosdk.live');
    }
    const meetingId = await createMeeting({ token });
    setMeetingId(meetingId);
  };

  return meetingId ? (
    <SafeAreaView style={{ flex: 1, backgroundColor: '#F6F6FF' }}>

      <MeetingProvider
        config={{
          meetingId,
          micEnabled: true,
          webcamEnabled: false,
          name: name,
        }}
        token={token}
        joinWithoutUserInteraction>
        <MeetingView setMeetingId={setMeetingId} />
      </MeetingProvider>
    </SafeAreaView>
  ) : (
    <JoinScreen
      getMeetingId={() => {
        getMeetingId();
      }}
      setName={setName}
      name={name}
    />
  );
}

