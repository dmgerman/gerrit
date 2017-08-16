begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ComparisonChain
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_class
DECL|class|AccountExternalIdInfo
specifier|public
class|class
name|AccountExternalIdInfo
implements|implements
name|Comparable
argument_list|<
name|AccountExternalIdInfo
argument_list|>
block|{
DECL|field|identity
specifier|public
name|String
name|identity
decl_stmt|;
DECL|field|emailAddress
specifier|public
name|String
name|emailAddress
decl_stmt|;
DECL|field|trusted
specifier|public
name|Boolean
name|trusted
decl_stmt|;
DECL|field|canDelete
specifier|public
name|Boolean
name|canDelete
decl_stmt|;
annotation|@
name|Override
DECL|method|compareTo (AccountExternalIdInfo a)
specifier|public
name|int
name|compareTo
parameter_list|(
name|AccountExternalIdInfo
name|a
parameter_list|)
block|{
return|return
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compare
argument_list|(
name|a
operator|.
name|identity
argument_list|,
name|identity
argument_list|)
operator|.
name|compare
argument_list|(
name|a
operator|.
name|emailAddress
argument_list|,
name|emailAddress
argument_list|)
operator|.
name|result
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|AccountExternalIdInfo
condition|)
block|{
name|AccountExternalIdInfo
name|a
init|=
operator|(
name|AccountExternalIdInfo
operator|)
name|o
decl_stmt|;
return|return
operator|(
name|Objects
operator|.
name|equals
argument_list|(
name|a
operator|.
name|identity
argument_list|,
name|identity
argument_list|)
operator|)
operator|&&
operator|(
name|Objects
operator|.
name|equals
argument_list|(
name|a
operator|.
name|emailAddress
argument_list|,
name|emailAddress
argument_list|)
operator|)
operator|&&
operator|(
name|Objects
operator|.
name|equals
argument_list|(
name|a
operator|.
name|trusted
argument_list|,
name|trusted
argument_list|)
operator|)
operator|&&
operator|(
name|Objects
operator|.
name|equals
argument_list|(
name|a
operator|.
name|canDelete
argument_list|,
name|canDelete
argument_list|)
operator|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|identity
argument_list|,
name|emailAddress
argument_list|,
name|trusted
argument_list|,
name|canDelete
argument_list|)
return|;
block|}
block|}
end_class

end_unit

