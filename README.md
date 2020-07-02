OMOK-2VS2
=============
건국대학교 협동분산시스템 프로젝트
---

#### 프로젝트 설명
기존의 오목과 달리 2명이 팀을 이루어 진행되는 오목 게임.<br/>
플레이어는 흑, 백, 관전자 중 하나의 자격으로 게임에 참여할 수 있습니다.<br/>

#### 게임 흐름
<font size="5">1. 게임 실행</font><br/>
- 입장 대기방<br/>
플레이어가 게임을 실행하면 아래와 같은 대기방으로 이동합니다. 왼쪽 리스트에 현재 생성된 게임방이 나타납니다. 방 이름 오른쪽에 현재 입장한 플레이어의 수가 나타납니다. 제한된 플레이어 수를 넘지 않았다면, 다른 플레이어들은 해당 방으로 입장할 수 있습니다.<br/>
- 게임 방 생성<br/>
플레이어는 하단 텍스트 입력 공간에 생성할 게임 방 이름을 입력하고 create 버튼을 눌러 게임 방을 생성할 수 있습니다.<br/>
![waiting_room](https://user-images.githubusercontent.com/55482623/86338930-e95fc600-bc8d-11ea-91da-37273911aea1.png)<br/><br/>
![make_game](https://user-images.githubusercontent.com/55482623/86339259-55422e80-bc8e-11ea-982a-993d59502490.png)
<br/><br/><br/>


<font size="10">2. 게임 대기방</font><br/>
- 게임 시작 전<br/>
플레이어가 게임 방에 입장하면, 관전자로 배정됩니다. 흑, 백, 관전자 팀은 최대 2명까지 배정받을 수 있습니다. 하단의 Black, Watch, White를 클릭하여 해당 팀으로 이동할 수 있습니다. <br/>
![enter_room](https://user-images.githubusercontent.com/55482623/86339593-bcf87980-bc8e-11ea-96a3-5926ed68419f.png)
<br/><br/>

- 채팅<br/>
게임 대기방에서는 채팅이 가능합니다. <br/>
![game_room_chat](https://user-images.githubusercontent.com/55482623/86341028-b7039800-bc90-11ea-985d-68958c8fe848.png)
<br/><br/>

- 준비<br/>
팀 선택을 완료했다면, 흑, 백팀은 ready를 눌러 준비 상태가 되어야 합니다. 모두 준비 상태가 되면 게임이 시작됩니다. <br/>
![ready](https://user-images.githubusercontent.com/55482623/86341267-0944b900-bc91-11ea-84b5-97062d4a3baf.png)
<br/><br/><br/>


<font size="5">3. 인게임</font><br/>
- 인게임 화면<br/>
게임이 시작되면, 플레이어는 두 가지 창을 제어할 수 있습니다. 하나는 오목판, 다른 하나는 자신의 팀원과 채팅할 수 있는 창입니다.<br/>
오목판의 왼쪽 상단에 현재 차례인 플레이어의 닉네임이 출력되며, 오른쪽 상단에는 제한 시간이 출력됩니다.<br/>
![오목판](https://user-images.githubusercontent.com/55482623/86342911-385c2a00-bc93-11ea-8a64-88bc0fd365a3.png)<br/>
![채팅창](https://user-images.githubusercontent.com/55482623/86342946-414cfb80-bc93-11ea-8861-875c090a58be.png)
<br/><br/>

- 게임 룰<br/>
플레이어는 자신의 차례에 오목알을 놓을 수 있습니다. 오목판을 한 번 클릭하면  해당 좌표가 회색 오목알로 표시되며, 해당 좌표를 한 번 더 클릭하면 그 곳에 오목알이 두어지게 됩니다. 회색 오목알은 같은 팀원에게만 보여집니다.<br/>
![회색 오목알](https://user-images.githubusercontent.com/55482623/86343019-588be900-bc93-11ea-8526-eefa7d49f44e.png)<br/>
![오목알 놓기](https://user-images.githubusercontent.com/55482623/86343062-680b3200-bc93-11ea-9b2b-a9b28c7ef881.png)
<br/><br/>

  각 플레이어는 제한 시간 안에 오목알을 놓아야 합니다. 그렇지 않으면 오목알을 두지 못한 채 상대 팀의 턴으로 넘어갑니다. <br/>
![타임아웃](https://user-images.githubusercontent.com/55482623/86343110-7b1e0200-bc93-11ea-864e-fb3c62a13c4e.png)
<br/><br/>

- 게임 종료<br/>
게임 종료 조건은 기존의 오목과 동일합니다. 게임이 종료되면, 각 플레이어의 창에 승리 / 패배 / 관전 종료 알림이 뜨고, 게임 대기방으로 돌아갑니다.<br/>
![승리](https://user-images.githubusercontent.com/55482623/86343209-9e48b180-bc93-11ea-888b-afb7a908d757.png)
<br/><br/>

##### Youtube link
https://www.youtube.com/watch?v=ytbjVq7mEtw&feature=youtu.be
