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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
operator|.
name|FORGE_AUTHOR
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
operator|.
name|FORGE_COMMITTER
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
operator|.
name|FORGE_IDENTITY
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
operator|.
name|FORGE_SERVER
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
operator|.
name|OWN
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
operator|.
name|PUSH_HEAD
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
operator|.
name|PUSH_HEAD_CREATE
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
operator|.
name|PUSH_HEAD_REPLACE
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
operator|.
name|PUSH_HEAD_UPDATE
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
operator|.
name|PUSH_TAG
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
operator|.
name|PUSH_TAG_ANNOTATED
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
operator|.
name|PUSH_TAG_SIGNED
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
operator|.
name|READ
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
name|reviewdb
operator|.
name|AccountGroup
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
name|reviewdb
operator|.
name|ApprovalCategory
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
name|reviewdb
operator|.
name|RefRight
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
name|CurrentUser
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
name|IdentifiedUser
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
name|RevCommit
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
name|RevObject
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
name|RevTag
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
name|RevWalk
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_comment
comment|/** Manages access control for Git references (aka branches, tags). */
end_comment

begin_class
DECL|class|RefControl
specifier|public
class|class
name|RefControl
block|{
DECL|field|projectControl
specifier|private
specifier|final
name|ProjectControl
name|projectControl
decl_stmt|;
DECL|field|refName
specifier|private
specifier|final
name|String
name|refName
decl_stmt|;
DECL|field|canForgeAuthor
specifier|private
name|Boolean
name|canForgeAuthor
decl_stmt|;
DECL|field|canForgeCommitter
specifier|private
name|Boolean
name|canForgeCommitter
decl_stmt|;
DECL|method|RefControl (final ProjectControl projectControl, final String refName)
name|RefControl
parameter_list|(
specifier|final
name|ProjectControl
name|projectControl
parameter_list|,
specifier|final
name|String
name|refName
parameter_list|)
block|{
name|this
operator|.
name|projectControl
operator|=
name|projectControl
expr_stmt|;
name|this
operator|.
name|refName
operator|=
name|refName
expr_stmt|;
block|}
DECL|method|getRefName ()
specifier|public
name|String
name|getRefName
parameter_list|()
block|{
return|return
name|refName
return|;
block|}
DECL|method|getProjectControl ()
specifier|public
name|ProjectControl
name|getProjectControl
parameter_list|()
block|{
return|return
name|projectControl
return|;
block|}
DECL|method|getCurrentUser ()
specifier|public
name|CurrentUser
name|getCurrentUser
parameter_list|()
block|{
return|return
name|getProjectControl
argument_list|()
operator|.
name|getCurrentUser
argument_list|()
return|;
block|}
DECL|method|forAnonymousUser ()
specifier|public
name|RefControl
name|forAnonymousUser
parameter_list|()
block|{
return|return
name|getProjectControl
argument_list|()
operator|.
name|forAnonymousUser
argument_list|()
operator|.
name|controlForRef
argument_list|(
name|getRefName
argument_list|()
argument_list|)
return|;
block|}
DECL|method|forUser (final CurrentUser who)
specifier|public
name|RefControl
name|forUser
parameter_list|(
specifier|final
name|CurrentUser
name|who
parameter_list|)
block|{
return|return
name|getProjectControl
argument_list|()
operator|.
name|forUser
argument_list|(
name|who
argument_list|)
operator|.
name|controlForRef
argument_list|(
name|getRefName
argument_list|()
argument_list|)
return|;
block|}
comment|/** Is this user a ref owner? */
DECL|method|isOwner ()
specifier|public
name|boolean
name|isOwner
parameter_list|()
block|{
if|if
condition|(
name|canPerform
argument_list|(
name|OWN
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// We have to prevent infinite recursion here, the project control
comment|// calls us to find out if there is ownership of all references in
comment|// order to determine project level ownership.
comment|//
if|if
condition|(
operator|!
name|RefRight
operator|.
name|ALL
operator|.
name|equals
argument_list|(
name|getRefName
argument_list|()
argument_list|)
operator|&&
name|getProjectControl
argument_list|()
operator|.
name|isOwner
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/** Can this user see this reference exists? */
DECL|method|isVisible ()
specifier|public
name|boolean
name|isVisible
parameter_list|()
block|{
return|return
name|getProjectControl
argument_list|()
operator|.
name|visibleForReplication
argument_list|()
operator|||
name|canPerform
argument_list|(
name|READ
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
return|;
block|}
comment|/**    * Determines whether the user can upload a change to the ref controlled by    * this object.    *    * @return {@code true} if the user specified can upload a change to the Git    *         ref    */
DECL|method|canUpload ()
specifier|public
name|boolean
name|canUpload
parameter_list|()
block|{
return|return
name|canPerform
argument_list|(
name|READ
argument_list|,
operator|(
name|short
operator|)
literal|2
argument_list|)
return|;
block|}
comment|/** @return true if the user can update the reference as a fast-forward. */
DECL|method|canUpdate ()
specifier|public
name|boolean
name|canUpdate
parameter_list|()
block|{
return|return
name|canPerform
argument_list|(
name|PUSH_HEAD
argument_list|,
name|PUSH_HEAD_UPDATE
argument_list|)
return|;
block|}
comment|/** @return true if the user can rewind (force push) the reference. */
DECL|method|canForceUpdate ()
specifier|public
name|boolean
name|canForceUpdate
parameter_list|()
block|{
return|return
name|canPerform
argument_list|(
name|PUSH_HEAD
argument_list|,
name|PUSH_HEAD_REPLACE
argument_list|)
operator|||
name|canDelete
argument_list|()
return|;
block|}
comment|/**    * Determines whether the user can create a new Git ref.    *    * @param rw revision pool {@code object} was parsed in.    * @param object the object the user will start the reference with.    * @return {@code true} if the user specified can create a new Git ref    */
DECL|method|canCreate (RevWalk rw, RevObject object)
specifier|public
name|boolean
name|canCreate
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|RevObject
name|object
parameter_list|)
block|{
name|boolean
name|owner
decl_stmt|;
switch|switch
condition|(
name|getCurrentUser
argument_list|()
operator|.
name|getAccessPath
argument_list|()
condition|)
block|{
case|case
name|WEB_UI
case|:
name|owner
operator|=
name|isOwner
argument_list|()
expr_stmt|;
break|break;
default|default:
name|owner
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|RevCommit
condition|)
block|{
return|return
name|owner
operator|||
name|canPerform
argument_list|(
name|PUSH_HEAD
argument_list|,
name|PUSH_HEAD_CREATE
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|object
operator|instanceof
name|RevTag
condition|)
block|{
specifier|final
name|RevTag
name|tag
init|=
operator|(
name|RevTag
operator|)
name|object
decl_stmt|;
try|try
block|{
name|rw
operator|.
name|parseBody
argument_list|(
name|tag
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
comment|// If tagger is present, require it matches the user's email.
comment|//
specifier|final
name|PersonIdent
name|tagger
init|=
name|tag
operator|.
name|getTaggerIdent
argument_list|()
decl_stmt|;
if|if
condition|(
name|tagger
operator|!=
literal|null
condition|)
block|{
name|boolean
name|valid
decl_stmt|;
if|if
condition|(
name|getCurrentUser
argument_list|()
operator|instanceof
name|IdentifiedUser
condition|)
block|{
specifier|final
name|IdentifiedUser
name|user
init|=
operator|(
name|IdentifiedUser
operator|)
name|getCurrentUser
argument_list|()
decl_stmt|;
specifier|final
name|String
name|addr
init|=
name|tagger
operator|.
name|getEmailAddress
argument_list|()
decl_stmt|;
name|valid
operator|=
name|user
operator|.
name|getEmailAddresses
argument_list|()
operator|.
name|contains
argument_list|(
name|addr
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|valid
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|valid
operator|&&
operator|!
name|owner
operator|&&
operator|!
name|canPerform
argument_list|(
name|FORGE_IDENTITY
argument_list|,
name|FORGE_COMMITTER
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|// If the tag has a PGP signature, allow a lower level of permission
comment|// than if it doesn't have a PGP signature.
comment|//
if|if
condition|(
name|tag
operator|.
name|getFullMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"-----BEGIN PGP SIGNATURE-----\n"
argument_list|)
condition|)
block|{
return|return
name|owner
operator|||
name|canPerform
argument_list|(
name|PUSH_TAG
argument_list|,
name|PUSH_TAG_SIGNED
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|owner
operator|||
name|canPerform
argument_list|(
name|PUSH_TAG
argument_list|,
name|PUSH_TAG_ANNOTATED
argument_list|)
return|;
block|}
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/**    * Determines whether the user can delete the Git ref controlled by this    * object.    *    * @return {@code true} if the user specified can delete a Git ref.    */
DECL|method|canDelete ()
specifier|public
name|boolean
name|canDelete
parameter_list|()
block|{
switch|switch
condition|(
name|getCurrentUser
argument_list|()
operator|.
name|getAccessPath
argument_list|()
condition|)
block|{
case|case
name|WEB_UI
case|:
return|return
name|isOwner
argument_list|()
operator|||
name|canPerform
argument_list|(
name|PUSH_HEAD
argument_list|,
name|PUSH_HEAD_REPLACE
argument_list|)
return|;
case|case
name|GIT
case|:
return|return
name|canPerform
argument_list|(
name|PUSH_HEAD
argument_list|,
name|PUSH_HEAD_REPLACE
argument_list|)
return|;
default|default:
return|return
literal|false
return|;
block|}
block|}
comment|/** @return true if this user can forge the author line in a commit. */
DECL|method|canForgeAuthor ()
specifier|public
name|boolean
name|canForgeAuthor
parameter_list|()
block|{
if|if
condition|(
name|canForgeAuthor
operator|==
literal|null
condition|)
block|{
name|canForgeAuthor
operator|=
name|canPerform
argument_list|(
name|FORGE_IDENTITY
argument_list|,
name|FORGE_AUTHOR
argument_list|)
expr_stmt|;
block|}
return|return
name|canForgeAuthor
return|;
block|}
comment|/** @return true if this user can forge the committer line in a commit. */
DECL|method|canForgeCommitter ()
specifier|public
name|boolean
name|canForgeCommitter
parameter_list|()
block|{
if|if
condition|(
name|canForgeCommitter
operator|==
literal|null
condition|)
block|{
name|canForgeCommitter
operator|=
name|canPerform
argument_list|(
name|FORGE_IDENTITY
argument_list|,
name|FORGE_COMMITTER
argument_list|)
expr_stmt|;
block|}
return|return
name|canForgeCommitter
return|;
block|}
comment|/** @return true if this user can forge the server on the committer line. */
DECL|method|canForgeGerritServerIdentity ()
specifier|public
name|boolean
name|canForgeGerritServerIdentity
parameter_list|()
block|{
return|return
name|canPerform
argument_list|(
name|FORGE_IDENTITY
argument_list|,
name|FORGE_SERVER
argument_list|)
return|;
block|}
comment|/**    * Convenience holder class used to map a ref pattern to the list of    * {@code RefRight}s that use it in the database.    */
DECL|class|RefRightsForPattern
specifier|public
specifier|final
specifier|static
class|class
name|RefRightsForPattern
block|{
DECL|field|rights
specifier|private
specifier|final
name|List
argument_list|<
name|RefRight
argument_list|>
name|rights
decl_stmt|;
DECL|field|containsExclusive
specifier|private
name|boolean
name|containsExclusive
decl_stmt|;
DECL|method|RefRightsForPattern ()
specifier|public
name|RefRightsForPattern
parameter_list|()
block|{
name|rights
operator|=
operator|new
name|ArrayList
argument_list|<
name|RefRight
argument_list|>
argument_list|()
expr_stmt|;
name|containsExclusive
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|addRight (RefRight right)
specifier|public
name|void
name|addRight
parameter_list|(
name|RefRight
name|right
parameter_list|)
block|{
name|rights
operator|.
name|add
argument_list|(
name|right
argument_list|)
expr_stmt|;
if|if
condition|(
name|right
operator|.
name|isExclusive
argument_list|()
condition|)
block|{
name|containsExclusive
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|method|getRights ()
specifier|public
name|List
argument_list|<
name|RefRight
argument_list|>
name|getRights
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|rights
argument_list|)
return|;
block|}
DECL|method|containsExclusive ()
specifier|public
name|boolean
name|containsExclusive
parameter_list|()
block|{
return|return
name|containsExclusive
return|;
block|}
comment|/**      * Returns The max allowed value for this ref pattern for all specified      * groups.      *      * @param groups The groups of the user      * @return The allowed value for this ref for all the specified groups      */
DECL|method|allowedValueForRef (Set<AccountGroup.Id> groups)
specifier|public
name|int
name|allowedValueForRef
parameter_list|(
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|groups
parameter_list|)
block|{
name|int
name|val
init|=
name|Integer
operator|.
name|MIN_VALUE
decl_stmt|;
for|for
control|(
name|RefRight
name|right
range|:
name|rights
control|)
block|{
if|if
condition|(
name|groups
operator|.
name|contains
argument_list|(
name|right
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
condition|)
block|{
name|val
operator|=
name|Math
operator|.
name|max
argument_list|(
name|right
operator|.
name|getMaxValue
argument_list|()
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|val
return|;
block|}
block|}
DECL|method|canPerform (ApprovalCategory.Id actionId, short level)
name|boolean
name|canPerform
parameter_list|(
name|ApprovalCategory
operator|.
name|Id
name|actionId
parameter_list|,
name|short
name|level
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|groups
init|=
name|getCurrentUser
argument_list|()
operator|.
name|getEffectiveGroups
argument_list|()
decl_stmt|;
name|int
name|val
init|=
name|Integer
operator|.
name|MIN_VALUE
decl_stmt|;
name|List
argument_list|<
name|RefRight
argument_list|>
name|allRights
init|=
operator|new
name|ArrayList
argument_list|<
name|RefRight
argument_list|>
argument_list|()
decl_stmt|;
name|allRights
operator|.
name|addAll
argument_list|(
name|getLocalRights
argument_list|(
name|actionId
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|actionId
operator|.
name|canInheritFromWildProject
argument_list|()
condition|)
block|{
name|allRights
operator|.
name|addAll
argument_list|(
name|getInheritedRights
argument_list|(
name|actionId
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|SortedMap
argument_list|<
name|String
argument_list|,
name|RefRightsForPattern
argument_list|>
name|perPatternRights
init|=
name|sortedRightsByPattern
argument_list|(
name|allRights
argument_list|)
decl_stmt|;
for|for
control|(
name|RefRightsForPattern
name|right
range|:
name|perPatternRights
operator|.
name|values
argument_list|()
control|)
block|{
name|val
operator|=
name|Math
operator|.
name|max
argument_list|(
name|val
argument_list|,
name|right
operator|.
name|allowedValueForRef
argument_list|(
name|groups
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|val
operator|>=
name|level
operator|||
name|right
operator|.
name|containsExclusive
argument_list|()
condition|)
block|{
return|return
name|val
operator|>=
name|level
return|;
block|}
block|}
return|return
name|val
operator|>=
name|level
return|;
block|}
DECL|field|DESCENDING_SORT
specifier|public
specifier|static
specifier|final
name|Comparator
argument_list|<
name|String
argument_list|>
name|DESCENDING_SORT
init|=
operator|new
name|Comparator
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|String
name|a
parameter_list|,
name|String
name|b
parameter_list|)
block|{
name|int
name|aLength
init|=
name|a
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|bLength
init|=
name|b
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|bLength
operator|==
name|aLength
condition|)
block|{
return|return
name|a
operator|.
name|compareTo
argument_list|(
name|b
argument_list|)
return|;
block|}
return|return
name|bLength
operator|-
name|aLength
return|;
block|}
block|}
decl_stmt|;
comment|/**    * Sorts all given rights into a map, ordered by descending length of    * ref pattern.    *    * For example, if given the following rights in argument:    *    * ["refs/heads/master", group1, -1, +1],    * ["refs/heads/master", group2, -2, +2],    * ["refs/heads/*", group3, -1, +1]    * ["refs/heads/stable", group2, -1, +1]    *    * Then the following map is returned:    * "refs/heads/master" => {    *      ["refs/heads/master", group1, -1, +1],    *      ["refs/heads/master", group2, -2, +2]    *  }    * "refs/heads/stable" => {["refs/heads/stable", group2, -1, +1]}    * "refs/heads/*" => {["refs/heads/*", group3, -1, +1]}    *    * @param actionRights    * @return A sorted map keyed off the ref pattern of all rights.    */
DECL|method|sortedRightsByPattern ( List<RefRight> actionRights)
specifier|private
specifier|static
name|SortedMap
argument_list|<
name|String
argument_list|,
name|RefRightsForPattern
argument_list|>
name|sortedRightsByPattern
parameter_list|(
name|List
argument_list|<
name|RefRight
argument_list|>
name|actionRights
parameter_list|)
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|RefRightsForPattern
argument_list|>
name|rights
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|RefRightsForPattern
argument_list|>
argument_list|(
name|DESCENDING_SORT
argument_list|)
decl_stmt|;
for|for
control|(
name|RefRight
name|actionRight
range|:
name|actionRights
control|)
block|{
name|RefRightsForPattern
name|patternRights
init|=
name|rights
operator|.
name|get
argument_list|(
name|actionRight
operator|.
name|getRefPattern
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|patternRights
operator|==
literal|null
condition|)
block|{
name|patternRights
operator|=
operator|new
name|RefRightsForPattern
argument_list|()
expr_stmt|;
name|rights
operator|.
name|put
argument_list|(
name|actionRight
operator|.
name|getRefPattern
argument_list|()
argument_list|,
name|patternRights
argument_list|)
expr_stmt|;
block|}
name|patternRights
operator|.
name|addRight
argument_list|(
name|actionRight
argument_list|)
expr_stmt|;
block|}
return|return
name|rights
return|;
block|}
DECL|method|getLocalRights (ApprovalCategory.Id actionId)
specifier|private
name|List
argument_list|<
name|RefRight
argument_list|>
name|getLocalRights
parameter_list|(
name|ApprovalCategory
operator|.
name|Id
name|actionId
parameter_list|)
block|{
return|return
name|filter
argument_list|(
name|getProjectState
argument_list|()
operator|.
name|getLocalRights
argument_list|(
name|actionId
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getInheritedRights (ApprovalCategory.Id actionId)
specifier|private
name|List
argument_list|<
name|RefRight
argument_list|>
name|getInheritedRights
parameter_list|(
name|ApprovalCategory
operator|.
name|Id
name|actionId
parameter_list|)
block|{
return|return
name|filter
argument_list|(
name|getProjectState
argument_list|()
operator|.
name|getInheritedRights
argument_list|(
name|actionId
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns all applicable rights for a given approval category.    *    * Applicable rights are defined as the list of {@code RefRight}s which match    * the ref for which this object was created, stopping the ref wildcard    * matching when an exclusive ref right was encountered, for the given    * approval category.    * @param id The {@link ApprovalCategory.Id}.    * @return All applicalbe rights.    */
DECL|method|getApplicableRights (final ApprovalCategory.Id id)
specifier|public
name|List
argument_list|<
name|RefRight
argument_list|>
name|getApplicableRights
parameter_list|(
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|id
parameter_list|)
block|{
name|List
argument_list|<
name|RefRight
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<
name|RefRight
argument_list|>
argument_list|()
decl_stmt|;
name|l
operator|.
name|addAll
argument_list|(
name|getLocalRights
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|l
operator|.
name|addAll
argument_list|(
name|getInheritedRights
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|SortedMap
argument_list|<
name|String
argument_list|,
name|RefRightsForPattern
argument_list|>
name|perPatternRights
init|=
name|sortedRightsByPattern
argument_list|(
name|l
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RefRight
argument_list|>
name|applicable
init|=
operator|new
name|ArrayList
argument_list|<
name|RefRight
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|RefRightsForPattern
name|patternRights
range|:
name|perPatternRights
operator|.
name|values
argument_list|()
control|)
block|{
name|applicable
operator|.
name|addAll
argument_list|(
name|patternRights
operator|.
name|getRights
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|patternRights
operator|.
name|containsExclusive
argument_list|()
condition|)
block|{
break|break;
block|}
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|applicable
argument_list|)
return|;
block|}
DECL|method|filter (Collection<RefRight> all)
specifier|private
name|List
argument_list|<
name|RefRight
argument_list|>
name|filter
parameter_list|(
name|Collection
argument_list|<
name|RefRight
argument_list|>
name|all
parameter_list|)
block|{
name|List
argument_list|<
name|RefRight
argument_list|>
name|mine
init|=
operator|new
name|ArrayList
argument_list|<
name|RefRight
argument_list|>
argument_list|(
name|all
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|RefRight
name|right
range|:
name|all
control|)
block|{
if|if
condition|(
name|matches
argument_list|(
name|getRefName
argument_list|()
argument_list|,
name|right
operator|.
name|getRefPattern
argument_list|()
argument_list|)
condition|)
block|{
name|mine
operator|.
name|add
argument_list|(
name|right
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|mine
return|;
block|}
DECL|method|getProjectState ()
specifier|private
name|ProjectState
name|getProjectState
parameter_list|()
block|{
return|return
name|projectControl
operator|.
name|getProjectState
argument_list|()
return|;
block|}
DECL|method|matches (String refName, String refPattern)
specifier|public
specifier|static
name|boolean
name|matches
parameter_list|(
name|String
name|refName
parameter_list|,
name|String
name|refPattern
parameter_list|)
block|{
if|if
condition|(
name|refPattern
operator|.
name|endsWith
argument_list|(
literal|"/*"
argument_list|)
condition|)
block|{
name|String
name|prefix
init|=
name|refPattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|refPattern
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
return|return
name|refName
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|refName
operator|.
name|equals
argument_list|(
name|refPattern
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

