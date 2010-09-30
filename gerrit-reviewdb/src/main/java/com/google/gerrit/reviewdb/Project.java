begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Column
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|StringKey
import|;
end_import

begin_comment
comment|/** Projects match a source code repository managed by Gerrit */
end_comment

begin_class
DECL|class|Project
specifier|public
specifier|final
class|class
name|Project
block|{
comment|/** Project name key */
DECL|class|NameKey
specifier|public
specifier|static
class|class
name|NameKey
extends|extends
name|StringKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|name
specifier|protected
name|String
name|name
decl_stmt|;
DECL|method|NameKey ()
specifier|protected
name|NameKey
parameter_list|()
block|{     }
DECL|method|NameKey (final String n)
specifier|public
name|NameKey
parameter_list|(
specifier|final
name|String
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|set (String newValue)
specifier|protected
name|void
name|set
parameter_list|(
name|String
name|newValue
parameter_list|)
block|{
name|name
operator|=
name|newValue
expr_stmt|;
block|}
comment|/** Parse a Project.NameKey out of a string representation. */
DECL|method|parse (final String str)
specifier|public
specifier|static
name|NameKey
name|parse
parameter_list|(
specifier|final
name|String
name|str
parameter_list|)
block|{
specifier|final
name|NameKey
name|r
init|=
operator|new
name|NameKey
argument_list|()
decl_stmt|;
name|r
operator|.
name|fromString
argument_list|(
name|str
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
block|}
DECL|enum|SubmitType
specifier|public
specifier|static
enum|enum
name|SubmitType
block|{
DECL|enumConstant|FAST_FORWARD_ONLY
name|FAST_FORWARD_ONLY
argument_list|(
literal|'F'
argument_list|)
block|,
DECL|enumConstant|MERGE_IF_NECESSARY
name|MERGE_IF_NECESSARY
argument_list|(
literal|'M'
argument_list|)
block|,
DECL|enumConstant|MERGE_ALWAYS
name|MERGE_ALWAYS
argument_list|(
literal|'A'
argument_list|)
block|,
DECL|enumConstant|CHERRY_PICK
name|CHERRY_PICK
argument_list|(
literal|'C'
argument_list|)
block|;
DECL|field|code
specifier|private
specifier|final
name|char
name|code
decl_stmt|;
DECL|method|SubmitType (final char c)
specifier|private
name|SubmitType
parameter_list|(
specifier|final
name|char
name|c
parameter_list|)
block|{
name|code
operator|=
name|c
expr_stmt|;
block|}
DECL|method|getCode ()
specifier|public
name|char
name|getCode
parameter_list|()
block|{
return|return
name|code
return|;
block|}
DECL|method|forCode (final char c)
specifier|public
specifier|static
name|SubmitType
name|forCode
parameter_list|(
specifier|final
name|char
name|c
parameter_list|)
block|{
for|for
control|(
specifier|final
name|SubmitType
name|s
range|:
name|SubmitType
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|s
operator|.
name|code
operator|==
name|c
condition|)
block|{
return|return
name|s
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|name
specifier|protected
name|NameKey
name|name
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|2
argument_list|,
name|length
operator|=
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|description
specifier|protected
name|String
name|description
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|3
argument_list|)
DECL|field|useContributorAgreements
specifier|protected
name|boolean
name|useContributorAgreements
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|4
argument_list|)
DECL|field|useSignedOffBy
specifier|protected
name|boolean
name|useSignedOffBy
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|5
argument_list|)
DECL|field|submitType
specifier|protected
name|char
name|submitType
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|6
argument_list|,
name|notNull
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"parent_name"
argument_list|)
DECL|field|parent
specifier|protected
name|NameKey
name|parent
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|7
argument_list|)
DECL|field|requireChangeID
specifier|protected
name|boolean
name|requireChangeID
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|8
argument_list|)
DECL|field|useContentMerge
specifier|protected
name|boolean
name|useContentMerge
decl_stmt|;
DECL|method|Project ()
specifier|protected
name|Project
parameter_list|()
block|{   }
DECL|method|Project (final Project.NameKey newName)
specifier|public
name|Project
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|newName
parameter_list|)
block|{
name|name
operator|=
name|newName
expr_stmt|;
name|useContributorAgreements
operator|=
literal|true
expr_stmt|;
name|setSubmitType
argument_list|(
name|SubmitType
operator|.
name|MERGE_IF_NECESSARY
argument_list|)
expr_stmt|;
block|}
DECL|method|getNameKey ()
specifier|public
name|Project
operator|.
name|NameKey
name|getNameKey
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (final String d)
specifier|public
name|void
name|setDescription
parameter_list|(
specifier|final
name|String
name|d
parameter_list|)
block|{
name|description
operator|=
name|d
expr_stmt|;
block|}
DECL|method|isUseContributorAgreements ()
specifier|public
name|boolean
name|isUseContributorAgreements
parameter_list|()
block|{
return|return
name|useContributorAgreements
return|;
block|}
DECL|method|setUseContributorAgreements (final boolean u)
specifier|public
name|void
name|setUseContributorAgreements
parameter_list|(
specifier|final
name|boolean
name|u
parameter_list|)
block|{
name|useContributorAgreements
operator|=
name|u
expr_stmt|;
block|}
DECL|method|isUseSignedOffBy ()
specifier|public
name|boolean
name|isUseSignedOffBy
parameter_list|()
block|{
return|return
name|useSignedOffBy
return|;
block|}
DECL|method|isUseContentMerge ()
specifier|public
name|boolean
name|isUseContentMerge
parameter_list|()
block|{
return|return
name|useContentMerge
return|;
block|}
DECL|method|isRequireChangeID ()
specifier|public
name|boolean
name|isRequireChangeID
parameter_list|()
block|{
return|return
name|requireChangeID
return|;
block|}
DECL|method|setUseSignedOffBy (final boolean sbo)
specifier|public
name|void
name|setUseSignedOffBy
parameter_list|(
specifier|final
name|boolean
name|sbo
parameter_list|)
block|{
name|useSignedOffBy
operator|=
name|sbo
expr_stmt|;
block|}
DECL|method|setUseContentMerge (final boolean cm)
specifier|public
name|void
name|setUseContentMerge
parameter_list|(
specifier|final
name|boolean
name|cm
parameter_list|)
block|{
name|useContentMerge
operator|=
name|cm
expr_stmt|;
block|}
DECL|method|setRequireChangeID (final boolean cid)
specifier|public
name|void
name|setRequireChangeID
parameter_list|(
specifier|final
name|boolean
name|cid
parameter_list|)
block|{
name|requireChangeID
operator|=
name|cid
expr_stmt|;
block|}
DECL|method|getSubmitType ()
specifier|public
name|SubmitType
name|getSubmitType
parameter_list|()
block|{
return|return
name|SubmitType
operator|.
name|forCode
argument_list|(
name|submitType
argument_list|)
return|;
block|}
DECL|method|setSubmitType (final SubmitType type)
specifier|public
name|void
name|setSubmitType
parameter_list|(
specifier|final
name|SubmitType
name|type
parameter_list|)
block|{
name|submitType
operator|=
name|type
operator|.
name|getCode
argument_list|()
expr_stmt|;
block|}
DECL|method|copySettingsFrom (final Project update)
specifier|public
name|void
name|copySettingsFrom
parameter_list|(
specifier|final
name|Project
name|update
parameter_list|)
block|{
name|description
operator|=
name|update
operator|.
name|description
expr_stmt|;
name|useContributorAgreements
operator|=
name|update
operator|.
name|useContributorAgreements
expr_stmt|;
name|useSignedOffBy
operator|=
name|update
operator|.
name|useSignedOffBy
expr_stmt|;
name|useContentMerge
operator|=
name|update
operator|.
name|useContentMerge
expr_stmt|;
name|requireChangeID
operator|=
name|update
operator|.
name|requireChangeID
expr_stmt|;
name|submitType
operator|=
name|update
operator|.
name|submitType
expr_stmt|;
block|}
DECL|method|getParent ()
specifier|public
name|Project
operator|.
name|NameKey
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
DECL|method|setParent (final Project.NameKey parentProjectName)
specifier|public
name|void
name|setParent
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|parentProjectName
parameter_list|)
block|{
name|parent
operator|=
name|parentProjectName
expr_stmt|;
block|}
block|}
end_class

end_unit

