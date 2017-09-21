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
DECL|package|com.google.gerrit.server.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|diff
operator|.
name|SequenceComparator
import|;
end_import

begin_class
DECL|class|CharTextComparator
class|class
name|CharTextComparator
extends|extends
name|SequenceComparator
argument_list|<
name|CharText
argument_list|>
block|{
annotation|@
name|Override
DECL|method|equals (CharText a, int ai, CharText b, int bi)
specifier|public
name|boolean
name|equals
parameter_list|(
name|CharText
name|a
parameter_list|,
name|int
name|ai
parameter_list|,
name|CharText
name|b
parameter_list|,
name|int
name|bi
parameter_list|)
block|{
return|return
name|a
operator|.
name|charAt
argument_list|(
name|ai
argument_list|)
operator|==
name|b
operator|.
name|charAt
argument_list|(
name|bi
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hash (CharText seq, int ptr)
specifier|public
name|int
name|hash
parameter_list|(
name|CharText
name|seq
parameter_list|,
name|int
name|ptr
parameter_list|)
block|{
return|return
name|seq
operator|.
name|charAt
argument_list|(
name|ptr
argument_list|)
return|;
block|}
block|}
end_class

end_unit

