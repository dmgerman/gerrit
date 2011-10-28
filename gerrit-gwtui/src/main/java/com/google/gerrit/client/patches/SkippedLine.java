begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.client.patches
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|patches
package|;
end_package

begin_class
DECL|class|SkippedLine
specifier|public
class|class
name|SkippedLine
block|{
DECL|field|a
specifier|private
name|int
name|a
decl_stmt|;
DECL|field|b
specifier|private
name|int
name|b
decl_stmt|;
DECL|field|sz
specifier|private
name|int
name|sz
decl_stmt|;
DECL|method|SkippedLine (int startA, int startB, int size)
specifier|public
name|SkippedLine
parameter_list|(
name|int
name|startA
parameter_list|,
name|int
name|startB
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|a
operator|=
name|startA
expr_stmt|;
name|b
operator|=
name|startB
expr_stmt|;
name|sz
operator|=
name|size
expr_stmt|;
block|}
DECL|method|getStartA ()
specifier|public
name|int
name|getStartA
parameter_list|()
block|{
return|return
name|a
return|;
block|}
DECL|method|getStartB ()
specifier|public
name|int
name|getStartB
parameter_list|()
block|{
return|return
name|b
return|;
block|}
DECL|method|getSize ()
specifier|public
name|int
name|getSize
parameter_list|()
block|{
return|return
name|sz
return|;
block|}
DECL|method|incrementStart (int n)
specifier|public
name|void
name|incrementStart
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|a
operator|+=
name|n
expr_stmt|;
name|b
operator|+=
name|n
expr_stmt|;
name|reduceSize
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
DECL|method|reduceSize (int n)
specifier|public
name|void
name|reduceSize
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|sz
operator|-=
name|n
expr_stmt|;
block|}
block|}
end_class

end_unit

