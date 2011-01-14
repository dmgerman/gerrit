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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
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
name|reviewdb
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|ProjectAccess
specifier|public
class|class
name|ProjectAccess
block|{
DECL|field|revision
specifier|protected
name|String
name|revision
decl_stmt|;
DECL|field|inheritsFrom
specifier|protected
name|Project
operator|.
name|NameKey
name|inheritsFrom
decl_stmt|;
DECL|field|local
specifier|protected
name|List
argument_list|<
name|AccessSection
argument_list|>
name|local
decl_stmt|;
DECL|field|ownerOf
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|ownerOf
decl_stmt|;
DECL|method|ProjectAccess ()
specifier|public
name|ProjectAccess
parameter_list|()
block|{   }
DECL|method|getRevision ()
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|revision
return|;
block|}
DECL|method|setRevision (String name)
specifier|public
name|void
name|setRevision
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|revision
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getInheritsFrom ()
specifier|public
name|Project
operator|.
name|NameKey
name|getInheritsFrom
parameter_list|()
block|{
return|return
name|inheritsFrom
return|;
block|}
DECL|method|setInheritsFrom (Project.NameKey name)
specifier|public
name|void
name|setInheritsFrom
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
block|{
name|inheritsFrom
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getLocal ()
specifier|public
name|List
argument_list|<
name|AccessSection
argument_list|>
name|getLocal
parameter_list|()
block|{
return|return
name|local
return|;
block|}
DECL|method|setLocal (List<AccessSection> as)
specifier|public
name|void
name|setLocal
parameter_list|(
name|List
argument_list|<
name|AccessSection
argument_list|>
name|as
parameter_list|)
block|{
name|local
operator|=
name|as
expr_stmt|;
block|}
DECL|method|isOwnerOf (AccessSection section)
specifier|public
name|boolean
name|isOwnerOf
parameter_list|(
name|AccessSection
name|section
parameter_list|)
block|{
return|return
name|getOwnerOf
argument_list|()
operator|.
name|contains
argument_list|(
name|section
operator|.
name|getRefPattern
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getOwnerOf ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getOwnerOf
parameter_list|()
block|{
return|return
name|ownerOf
return|;
block|}
DECL|method|setOwnerOf (Set<String> refs)
specifier|public
name|void
name|setOwnerOf
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|refs
parameter_list|)
block|{
name|ownerOf
operator|=
name|refs
expr_stmt|;
block|}
block|}
end_class

end_unit

