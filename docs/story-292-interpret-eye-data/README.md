## GazeAnalysisStrategy 

Interface for GatherEye to determine what message to send to Broker

createMessage(float x, float y);

Three classes implement this interface:
PointAnalysis returns "GAZE,{x},{y}"
AreaAnalysis returns "GAZE AREA: X;Y;WIDTH;HEIGHT,{x},{y},{width},{height}"
VectorAnalysis returns "GAZE VECTOR: HEAD-X;HEAD-Y;TAIL-X;TAIL-Y,{},{},{},{}"
