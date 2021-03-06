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
DECL|package|com.google.gerrit.server.group.db
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
operator|.
name|db
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
name|ImmutableSet
import|;
end_import

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
name|Sets
import|;
end_import

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
name|Streams
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
operator|.
name|InternalGroup
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringJoiner
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiFunction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|revwalk
operator|.
name|FooterKey
import|;
end_import

begin_comment
comment|/**  * A parsable commit message for a NoteDb commit of a group.  *  *<p>For group creations, it's sufficient to simply call the constructor of this class. For  * updates, {@link #setOriginalGroup(InternalGroup)} has to be called as well.  */
end_comment

begin_class
DECL|class|GroupConfigCommitMessage
class|class
name|GroupConfigCommitMessage
block|{
DECL|field|FOOTER_ADD_MEMBER
specifier|static
specifier|final
name|FooterKey
name|FOOTER_ADD_MEMBER
init|=
operator|new
name|FooterKey
argument_list|(
literal|"Add"
argument_list|)
decl_stmt|;
DECL|field|FOOTER_REMOVE_MEMBER
specifier|static
specifier|final
name|FooterKey
name|FOOTER_REMOVE_MEMBER
init|=
operator|new
name|FooterKey
argument_list|(
literal|"Remove"
argument_list|)
decl_stmt|;
DECL|field|FOOTER_ADD_GROUP
specifier|static
specifier|final
name|FooterKey
name|FOOTER_ADD_GROUP
init|=
operator|new
name|FooterKey
argument_list|(
literal|"Add-group"
argument_list|)
decl_stmt|;
DECL|field|FOOTER_REMOVE_GROUP
specifier|static
specifier|final
name|FooterKey
name|FOOTER_REMOVE_GROUP
init|=
operator|new
name|FooterKey
argument_list|(
literal|"Remove-group"
argument_list|)
decl_stmt|;
DECL|field|auditLogFormatter
specifier|private
specifier|final
name|AuditLogFormatter
name|auditLogFormatter
decl_stmt|;
DECL|field|updatedGroup
specifier|private
specifier|final
name|InternalGroup
name|updatedGroup
decl_stmt|;
DECL|field|originalGroup
specifier|private
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|originalGroup
init|=
name|Optional
operator|.
name|empty
argument_list|()
decl_stmt|;
DECL|method|GroupConfigCommitMessage (AuditLogFormatter auditLogFormatter, InternalGroup updatedGroup)
name|GroupConfigCommitMessage
parameter_list|(
name|AuditLogFormatter
name|auditLogFormatter
parameter_list|,
name|InternalGroup
name|updatedGroup
parameter_list|)
block|{
name|this
operator|.
name|auditLogFormatter
operator|=
name|auditLogFormatter
expr_stmt|;
name|this
operator|.
name|updatedGroup
operator|=
name|updatedGroup
expr_stmt|;
block|}
DECL|method|setOriginalGroup (InternalGroup originalGroup)
specifier|public
name|void
name|setOriginalGroup
parameter_list|(
name|InternalGroup
name|originalGroup
parameter_list|)
block|{
name|this
operator|.
name|originalGroup
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|originalGroup
argument_list|)
expr_stmt|;
block|}
DECL|method|create ()
specifier|public
name|String
name|create
parameter_list|()
block|{
name|String
name|summaryLine
init|=
name|originalGroup
operator|.
name|isPresent
argument_list|()
condition|?
literal|"Update group"
else|:
literal|"Create group"
decl_stmt|;
name|StringJoiner
name|footerJoiner
init|=
operator|new
name|StringJoiner
argument_list|(
literal|"\n"
argument_list|,
literal|"\n\n"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|footerJoiner
operator|.
name|setEmptyValue
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|Streams
operator|.
name|concat
argument_list|(
name|Streams
operator|.
name|stream
argument_list|(
name|getFooterForRename
argument_list|()
argument_list|)
argument_list|,
name|getFootersForMemberModifications
argument_list|()
argument_list|,
name|getFootersForSubgroupModifications
argument_list|()
argument_list|)
operator|.
name|sorted
argument_list|()
operator|.
name|forEach
argument_list|(
name|footerJoiner
operator|::
name|add
argument_list|)
expr_stmt|;
name|String
name|footer
init|=
name|footerJoiner
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|summaryLine
operator|+
name|footer
return|;
block|}
DECL|method|getFooterForRename ()
specifier|private
name|Optional
argument_list|<
name|String
argument_list|>
name|getFooterForRename
parameter_list|()
block|{
if|if
condition|(
operator|!
name|originalGroup
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
name|String
name|originalName
init|=
name|originalGroup
operator|.
name|get
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|newName
init|=
name|updatedGroup
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|originalName
operator|.
name|equals
argument_list|(
name|newName
argument_list|)
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
return|return
name|Optional
operator|.
name|of
argument_list|(
literal|"Rename from "
operator|+
name|originalName
operator|+
literal|" to "
operator|+
name|newName
argument_list|)
return|;
block|}
DECL|method|getFootersForMemberModifications ()
specifier|private
name|Stream
argument_list|<
name|String
argument_list|>
name|getFootersForMemberModifications
parameter_list|()
block|{
return|return
name|getFooters
argument_list|(
name|InternalGroup
operator|::
name|getMembers
argument_list|,
name|AuditLogFormatter
operator|::
name|getParsableAccount
argument_list|,
name|FOOTER_ADD_MEMBER
argument_list|,
name|FOOTER_REMOVE_MEMBER
argument_list|)
return|;
block|}
DECL|method|getFootersForSubgroupModifications ()
specifier|private
name|Stream
argument_list|<
name|String
argument_list|>
name|getFootersForSubgroupModifications
parameter_list|()
block|{
return|return
name|getFooters
argument_list|(
name|InternalGroup
operator|::
name|getSubgroups
argument_list|,
name|AuditLogFormatter
operator|::
name|getParsableGroup
argument_list|,
name|FOOTER_ADD_GROUP
argument_list|,
name|FOOTER_REMOVE_GROUP
argument_list|)
return|;
block|}
DECL|method|getFooters ( Function<InternalGroup, Set<T>> getElements, BiFunction<AuditLogFormatter, T, String> toParsableString, FooterKey additionFooterKey, FooterKey removalFooterKey)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|Stream
argument_list|<
name|String
argument_list|>
name|getFooters
parameter_list|(
name|Function
argument_list|<
name|InternalGroup
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|getElements
parameter_list|,
name|BiFunction
argument_list|<
name|AuditLogFormatter
argument_list|,
name|T
argument_list|,
name|String
argument_list|>
name|toParsableString
parameter_list|,
name|FooterKey
name|additionFooterKey
parameter_list|,
name|FooterKey
name|removalFooterKey
parameter_list|)
block|{
name|Set
argument_list|<
name|T
argument_list|>
name|oldElements
init|=
name|originalGroup
operator|.
name|map
argument_list|(
name|getElements
argument_list|)
operator|.
name|orElseGet
argument_list|(
name|ImmutableSet
operator|::
name|of
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|T
argument_list|>
name|newElements
init|=
name|getElements
operator|.
name|apply
argument_list|(
name|updatedGroup
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|T
argument_list|,
name|String
argument_list|>
name|toString
init|=
name|element
lambda|->
name|toParsableString
operator|.
name|apply
argument_list|(
name|auditLogFormatter
argument_list|,
name|element
argument_list|)
decl_stmt|;
name|Stream
argument_list|<
name|String
argument_list|>
name|removedElements
init|=
name|Sets
operator|.
name|difference
argument_list|(
name|oldElements
argument_list|,
name|newElements
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|toString
argument_list|)
operator|.
name|map
argument_list|(
operator|(
name|removalFooterKey
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|)
operator|::
name|concat
argument_list|)
decl_stmt|;
name|Stream
argument_list|<
name|String
argument_list|>
name|addedElements
init|=
name|Sets
operator|.
name|difference
argument_list|(
name|newElements
argument_list|,
name|oldElements
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|toString
argument_list|)
operator|.
name|map
argument_list|(
operator|(
name|additionFooterKey
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|)
operator|::
name|concat
argument_list|)
decl_stmt|;
return|return
name|Stream
operator|.
name|concat
argument_list|(
name|removedElements
argument_list|,
name|addedElements
argument_list|)
return|;
block|}
block|}
end_class

end_unit

