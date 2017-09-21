begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
package|;
end_package

begin_class
DECL|class|MetadataName
specifier|public
specifier|final
class|class
name|MetadataName
block|{
DECL|field|CHANGE_NUMBER
specifier|public
specifier|static
specifier|final
name|String
name|CHANGE_NUMBER
init|=
literal|"Gerrit-Change-Number"
decl_stmt|;
DECL|field|PATCH_SET
specifier|public
specifier|static
specifier|final
name|String
name|PATCH_SET
init|=
literal|"Gerrit-PatchSet"
decl_stmt|;
DECL|field|MESSAGE_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_TYPE
init|=
literal|"Gerrit-MessageType"
decl_stmt|;
DECL|field|TIMESTAMP
specifier|public
specifier|static
specifier|final
name|String
name|TIMESTAMP
init|=
literal|"Gerrit-Comment-Date"
decl_stmt|;
DECL|method|toHeader (String metadataName)
specifier|public
specifier|static
name|String
name|toHeader
parameter_list|(
name|String
name|metadataName
parameter_list|)
block|{
return|return
literal|"X-"
operator|+
name|metadataName
return|;
block|}
DECL|method|toHeaderWithDelimiter (String metadataName)
specifier|public
specifier|static
name|String
name|toHeaderWithDelimiter
parameter_list|(
name|String
name|metadataName
parameter_list|)
block|{
return|return
name|toHeader
argument_list|(
name|metadataName
argument_list|)
operator|+
literal|": "
return|;
block|}
DECL|method|toFooterWithDelimiter (String metadataName)
specifier|public
specifier|static
name|String
name|toFooterWithDelimiter
parameter_list|(
name|String
name|metadataName
parameter_list|)
block|{
return|return
name|metadataName
operator|+
literal|": "
return|;
block|}
block|}
end_class

end_unit

