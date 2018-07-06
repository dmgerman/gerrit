begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.ioutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
package|;
end_package

begin_class
DECL|class|HexFormat
specifier|public
class|class
name|HexFormat
block|{
DECL|method|fromInt (int id)
specifier|public
specifier|static
name|String
name|fromInt
parameter_list|(
name|int
name|id
parameter_list|)
block|{
specifier|final
name|char
index|[]
name|r
init|=
operator|new
name|char
index|[
literal|8
index|]
decl_stmt|;
for|for
control|(
name|int
name|p
init|=
literal|7
init|;
literal|0
operator|<=
name|p
condition|;
name|p
operator|--
control|)
block|{
specifier|final
name|int
name|h
init|=
name|id
operator|&
literal|0xf
decl_stmt|;
name|r
index|[
name|p
index|]
operator|=
name|h
operator|<
literal|10
condition|?
call|(
name|char
call|)
argument_list|(
literal|'0'
operator|+
name|h
argument_list|)
else|:
call|(
name|char
call|)
argument_list|(
literal|'a'
operator|+
operator|(
name|h
operator|-
literal|10
operator|)
argument_list|)
expr_stmt|;
name|id
operator|>>=
literal|4
expr_stmt|;
block|}
return|return
operator|new
name|String
argument_list|(
name|r
argument_list|)
return|;
block|}
block|}
end_class

end_unit

