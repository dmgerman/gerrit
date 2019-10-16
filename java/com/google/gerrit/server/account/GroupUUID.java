begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
operator|.
name|AccountGroup
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
import|;
end_import

begin_class
DECL|class|GroupUUID
specifier|public
class|class
name|GroupUUID
block|{
DECL|method|make (String groupName, PersonIdent creator)
specifier|public
specifier|static
name|AccountGroup
operator|.
name|UUID
name|make
parameter_list|(
name|String
name|groupName
parameter_list|,
name|PersonIdent
name|creator
parameter_list|)
block|{
name|MessageDigest
name|md
init|=
name|Constants
operator|.
name|newMessageDigest
argument_list|()
decl_stmt|;
name|md
operator|.
name|update
argument_list|(
name|Constants
operator|.
name|encode
argument_list|(
literal|"group "
operator|+
name|groupName
operator|+
literal|"\n"
argument_list|)
argument_list|)
expr_stmt|;
name|md
operator|.
name|update
argument_list|(
name|Constants
operator|.
name|encode
argument_list|(
literal|"creator "
operator|+
name|creator
operator|.
name|toExternalString
argument_list|()
operator|+
literal|"\n"
argument_list|)
argument_list|)
expr_stmt|;
name|md
operator|.
name|update
argument_list|(
name|Constants
operator|.
name|encode
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Math
operator|.
name|random
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|AccountGroup
operator|.
name|uuid
argument_list|(
name|ObjectId
operator|.
name|fromRaw
argument_list|(
name|md
operator|.
name|digest
argument_list|()
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
DECL|method|GroupUUID ()
specifier|private
name|GroupUUID
parameter_list|()
block|{}
block|}
end_class

end_unit

